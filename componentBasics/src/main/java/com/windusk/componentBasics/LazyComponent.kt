package com.windusk.componentBasics

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import androidx.annotation.CallSuper
import com.windusk.componentBasics.sharing.Sharing
import com.windusk.componentBasics.sharing.lib.bite.BiteConstructor
import com.windusk.componentBasics.sharing.lib.componentInfo.ComponentInfoSharing
import com.windusk.componentBasics.sharing.lib.sharedFile.FileSharing
import com.windusk.ecosystem.CallbackMessenger
import com.windusk.ecosystem.logger.EcosystemLogger
import com.windusk.ecosystem.logger.EcosystemLogger.Companion.collectWithLog
import com.windusk.ecosystem.logger.LogInterface
import com.windusk.ecosystem.logger.LogOutput
import com.windusk.ecosystem.sharing.SharingAssociation
import com.windusk.ecosystem.sharing.SharingInfo
import com.windusk.ecosystemBasics.sharing.componentInfo.ComponentInfo
import com.windusk.ecosystemBasics.sharing.componentInfo.ComponentInfoSharingConsts
import com.windusk.ecosystemSharedFile.FileSharingConsts
import com.windusk.ecosystemSharedFile.fileDescriptor.ResourceDescriptorCreator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class LazyComponent : Service() {
    companion object {
        fun getLogger(clazz: Class<out LazyComponent>) = EcosystemLogger("LAZY COMPONENT: ${clazz.name}")
    }

    private val logger = getLogger(this::class.java)

    protected val scope = CoroutineScope(Dispatchers.IO)
    protected val binderMessenger by lazy {
        BinderMessengerImpl(
            stateMutable,
            callbackMessengerMutable
        )
    }

    protected open val sharings: List<Sharing> = emptyList()

    private val basicSharings by lazy {
        buildList {
            iconRes?.let {
                add(
                    FileSharing(
                        FileSharingConsts.IMAGE_TAG,
                        FileSharingConsts.KEY_COMPONENT_ICON,
                        flowOf(SharingInfo("Иконка компонента")),
                        fileFlow = flowOf(
                            ResourceDescriptorCreator(this@LazyComponent, it, "icon")
                        )
                    )
                )
            }
            add(
                ComponentInfoSharing(
                    ComponentInfoSharingConsts.KEY_MAIN,
                    infoFlow = info
                )
            )
            addAll(bite?.buildSharings().orEmpty())
        }
    }

    private val stateMutable = MutableStateFlow(false)
    private val callbackMessengerMutable = MutableStateFlow<CallbackMessenger?>(null)

    val state = stateMutable.asStateFlow()
    val callbackMessenger = callbackMessengerMutable.asStateFlow()
    val haveCallbackMessenger = callbackMessengerMutable.map { it != null }

    val work by lazy {
        combine(state, haveCallbackMessenger) { triggers -> triggers.all { it } }
            .distinctUntilChanged()
            .stateIn(scope, SharingStarted.Lazily, false)
    }

    protected abstract val info: Flow<ComponentInfo>

    protected open val iconRes: Int? = null
    protected open val bite: BiteConstructor? = null

    private val allSharings by lazy { basicSharings + sharings }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val sharingsFormated by lazy {
        allSharings
            .map { it.getOutputWithId() }.asFlow()
            .flattenMerge(allSharings.size)
    }

    @CallSuper
    open fun onInit() {
        launchProviding(scope)
    }

    @CallSuper
    open fun onDead() {
        callbackMessengerMutable.tryEmit(null)
        scope.cancel()
    }

    override fun onBind(intent: Intent?): Binder {
        logger.i("Component bound.")
        return binderMessenger
    }

    override fun onUnbind(intent: Intent?): Boolean {
        logger.i("Component unbound.")

        callbackMessengerMutable.tryEmit(null)
        return false
    }

    override fun onCreate() {
        super.onCreate()
        onInit()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDead()
    }

    protected fun bite(
        constructor: BiteConstructor.() -> Unit
    ) = BiteConstructor.create(scope, constructor)

    private fun launchProviding(scope: CoroutineScope) {
        val temp = mutableMapOf<SharingAssociation, Bundle>()

        scope.launch {
            sharingsFormated.collectWithLog(logger, "NEW SHARING OUTPUT") { (association, output) ->
                temp[association] = output.export()

                try {
                    callbackMessenger.value?.updateSharing(association, temp[association])
                } catch (e: Throwable) {
                    return@collectWithLog LogOutput(
                        LogInterface.ERROR,
                        "SAVED OUTPUT\nASSOCIATION: $association\nOUTPUT: ${temp[association]}\nERROR: ${e.stackTraceToString()}"
                    )
                }

                LogOutput(
                    LogInterface.INFO,
                    "SAVED OUTPUT\nASSOCIATION: $association\nOUTPUT: ${temp[association]}"
                )
            }
        }

        scope.launch {
            callbackMessenger.collectWithLog(logger, "CALLBACK MESSENGER (FOR PROVIDING)") { messenger ->
                if(messenger == null)
                    return@collectWithLog LogOutput(LogInterface.WARNING, "CALLBACK MESSENGER is null.")

                temp.forEach {
                    try {
                        messenger.updateSharing(it.key, it.value)
                    } catch (e: Throwable) {
                        logger.e(
                            "NEW CALLBACK MESSENGER\nASSOCIATION: ${it.key}\nOUTPUT: ${it.value}\nERROR: ${e.stackTraceToString()}"
                        )
                    }
                }

                LogOutput(LogInterface.INFO, "NEW CALLBACK MESSENGER. ${temp.size}")
            }
        }
    }
}