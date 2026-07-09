package com.windusk.clientBasics.component

import android.content.ComponentName
import android.content.Context
import com.windusk.clientBasics.data.factory.EcosystemFactory
import com.windusk.clientBasics.data.savedComponents.database.SavedComponent
import com.windusk.clientBasics.domain.ComponentBitePage
import com.windusk.clientBasics.getSharingDelegatedFlow
import com.windusk.clientBasics.service.error.stackTraceToComponentErrorString
import com.windusk.clientBasics.service.messenger.CallbackMessengerImpl
import com.windusk.componentBasics.sharing.lib.componentInfo.ComponentInfoSubscriberDelegate
import com.windusk.componentBasics.sharing.lib.sharedFile.FileSubscriberDelegate
import com.windusk.ecosystemBasics.sharing.bite.BiteSharingConsts
import com.windusk.ecosystemBasics.sharing.componentInfo.ComponentInfoSharingConsts
import com.windusk.ecosystemBasics.sharing.settings.SettingsViewSharingConsts
import com.windusk.ecosystemBiteUi.view.BlockView
import com.windusk.ecosystemSharedFile.FileSharingConsts
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration.Companion.seconds

class Component(
    val name: ComponentName,
    private val context: Context,
    private val scope: CoroutineScope,
    private val ecosystemFactory: EcosystemFactory
) {
    data class Error(
        val blockName: String,
        val error: Throwable
    )

    enum class State {
        ENABLED,
        DISABLED,
        DONT_BINDED,
        CRASHED,
        DONT_ALLOWED
    }

    private val mutex = Mutex()

    private var updateStateJob: Job? = null
    private var job: Job? = null

    private val buffer = object : ComponentBuffer {
        override val defaultBlockName = "Запрос к компоненту"

        override fun <T> run(
            blockName: String,
            crashOnError: Boolean,
            block: () -> T
        ) = try {
            block()
        } catch (e: Throwable) {
            if(crashOnError) crash(blockName, e)
            null
        }

        override fun <T> run(
            blockName: String,
            crashIf: (e: Throwable) -> Boolean,
            block: () -> T
        ) = try {
            block()
        } catch (e: Throwable) {
            if(crashIf(e)) crash(blockName, e)
            null
        }

        override fun successRun(
            blockName: String,
            crashOnError: Boolean,
            block: () -> Unit
        ) = try {
            block()
            true
        } catch (e: Throwable) {
            if(crashOnError) crash(blockName, e)
            false
        }

        override fun successRun(
            blockName: String,
            crashIf: (e: Throwable) -> Boolean,
            block: () -> Unit
        ) = try {
            block()
            true
        } catch (e: Throwable) {
            if(crashIf(e)) crash(blockName, e)
            false
        }

        override fun launch(
            blockName: String,
            crashOnError: Boolean,
            block: suspend () -> Unit
        ): Job = scope.launch {
            try {
                block()
            } catch (e: Throwable) {
                if(crashOnError) crash(blockName, e)
            }
        }

        override fun launch(
            blockName: String,
            crashIf: (e: Throwable) -> Boolean,
            block: suspend () -> Unit
        ): Job = scope.launch {
            try {
                block()
            } catch (e: Throwable) {
                if(crashIf(e)) crash(blockName, e)
            }
        }

        override fun throwable(blockName: String, crash: Boolean) {
            if(crash) crash(blockName, Throwable())
        }
    }

    private val callbackMessenger = CallbackMessengerImpl(this, ecosystemFactory)
    private val errorMutable = MutableStateFlow<Error?>(null)

    private val localSettings = ComponentLocalSettings(name, buffer, ecosystemFactory.componentRepository)
    private val binder = ComponentBinder(name, context, buffer, callbackMessenger)

    val savedLocalSettings = localSettings.saved
    val error = errorMutable.asStateFlow()

    val state = combine(
        errorMutable,
        localSettings.saved,
        binder.messenger
    ) { currentError, currentSaved, currentMessenger ->
        when {
            !currentSaved.allowed -> State.DONT_ALLOWED
            currentError != null -> State.CRASHED
            currentMessenger == null -> State.DONT_BINDED
            else -> if(currentSaved.enabled) State.ENABLED else State.DISABLED
        }
    }.stateIn(scope, started = SharingStarted.Eagerly, State.DONT_BINDED)

    val icon = ecosystemFactory.sharingFactory.sharingSaver.getSharingDelegatedFlow(
        name,
        FileSubscriberDelegate(FileSharingConsts.IMAGE_TAG) {
            crash("Загрузка иконки компонента", it)
        },
        FileSharingConsts.KEY_COMPONENT_ICON
    )

    val info = ecosystemFactory.sharingFactory.sharingSaver.getSharingDelegatedFlow(
        name,
        ComponentInfoSubscriberDelegate(),
        ComponentInfoSharingConsts.KEY_MAIN
    )

    val pages = ecosystemFactory.sharingFactory.sharingSaver.getSavedFlow().map { list ->
        buffer.run("Получение страницы компонента") {
            val sharings = list.filterKeys {
                it.getComponentName() == name && it.dataType == BiteSharingConsts.DATA_TYPE && it.tag == SettingsViewSharingConsts.TAG
            }

            sharings.map { (association, output) ->
                var error: Error? = null

                val info = buffer.run(
                    "Получение информации о странице компонента",
                    crashIf = {
                        error = Error("Получение информации о странице компонента", it)
                        false
                    }
                ) {
                    output.getInfo()
                } ?: return@map ComponentBitePage(
                    association.key,
                    null,
                    null,
                    error,
                    null
                )

                val data = buffer.run(
                    "Получение внутренностей страницы компонента",
                    crashIf = {
                        error = Error("Получение внутренностей страницы компонента", it)
                        false
                    }
                ) {
                    val data = output.data.get()
                    
                    data.classLoader = BlockView::class.java.classLoader
                    (data.getParcelable(BiteSharingConsts.BUNDLE_EXTRA_VIEW) as? BlockView)!!
                } ?: return@map ComponentBitePage(
                    association.key,
                    null,
                    null,
                    error,
                    null
                )

                ComponentBitePage(
                    association.key,
                    info.name,
                    info.description,
                    error,
                    data
                )
            }
        }.orEmpty()
    }

    fun init() {
        close()
        updateStateJob = scope.launch {
            combine(
                errorMutable,
                localSettings.saved,
                binder.messenger
            ) { currentError, currentSaved, currentMessenger ->
                updateJob(currentError, currentSaved, currentMessenger)
            }.collect()
        }
    }

    fun close() {
        updateStateJob?.cancel()
        updateStateJob = null
        job?.cancel()
        job = null
    }

    private suspend fun updateJob(
        currentError: Error?,
        currentSaved: SavedComponent,
        currentMessenger: ComponentMessenger?
    ) {
        mutex.withLock {
            job?.cancel()
            job = buffer.launch {
                if(!currentSaved.allowed || currentError != null) {
                    binder.unbind()
                    job = null
                    return@launch
                }

                if(currentMessenger == null) {
                    while(true) {
                        binder.rebind()
                        delay(10.seconds)
                    }
                }

                binder.prepare()

                while(true) {
                    delay(1.seconds)
                    currentMessenger.setState(currentSaved.enabled)
                }
            }
        }
    }

    fun <T>runIfState(
        vararg allowedStates: State,
        blockName: String,
        block: () -> T
    ): T? = buffer.run(blockName) {
        if(state.value in allowedStates) block() else null
    }

    fun crash(
        blockName: String,
        e: Throwable
    ) {
        if(e is CancellationException) return
        errorMutable.tryEmit(Error(blockName, e))
    }

    fun clearError() {
        errorMutable.tryEmit(null)
    }

    fun setAllowed(state: Boolean) = buffer
        .launch("Изменение локальных настроек компонента") { localSettings.setAllowed(state) }

    fun setEnabled(state: Boolean) = buffer
        .launch("Изменение локальных настроек компонента") { localSettings.setEnabled(state) }

    fun set(allowed: Boolean? = null, enabled: Boolean? = null) = buffer
        .launch("Изменение локальных настроек компонента") { localSettings.set(allowed, enabled) }
}