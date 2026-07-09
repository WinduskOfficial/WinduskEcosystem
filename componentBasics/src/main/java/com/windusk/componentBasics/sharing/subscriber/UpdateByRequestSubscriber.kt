package com.windusk.componentBasics.sharing.subscriber

import android.os.Bundle
import androidx.annotation.CallSuper
import com.windusk.componentBasics.LazyComponentTransmitter
import com.windusk.componentBasics.sharing.delegate.SubscriberDelegate
import com.windusk.ecosystem.logger.EcosystemLogger.Companion.collectWithLog
import com.windusk.ecosystem.logger.LogInterface
import com.windusk.ecosystem.logger.LogOutput
import com.windusk.ecosystem.subscribition.SubscribitionOutput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

open class UpdateByRequestSubscriber<T>(
    val scope: CoroutineScope,
    val componentTransmitter: LazyComponentTransmitter<*>
) : ComponentSubscriber<T>(scope, componentTransmitter) {
    companion object {
        fun <T>create(
            scope: CoroutineScope,
            componentTransmitter: LazyComponentTransmitter<*>,
            delegate: SubscriberDelegate<T>
        ) = UpdateByRequestSubscriber<T>(scope, componentTransmitter).apply { this.delegate = delegate }
    }

    private val needToUpdate = MutableStateFlow(false)
    private var lastOutput = MutableStateFlow<SubscribitionOutput?>(null)

    private val outputWithNeedToUpdate = combine(lastOutput, needToUpdate) { currentOutput, currentNeedToUpdate ->
        currentOutput.takeIf { currentNeedToUpdate }
    }

    private var updating: Job? = null

    override fun init() {
        super.init()

        scope.launch {
            outputWithNeedToUpdate.collectWithLog(componentTransmitter.logger, "PRIORITY SUBSCRIBER (UPDATE MANUALLY)") { currentOutput ->
                if(currentOutput == null) return@collectWithLog LogOutput(LogInterface.INFO, "SKIP OUTPUT")

                onOutputUpdate(currentOutput.data.get())
                LogOutput(LogInterface.INFO, "OUTPUT UPDATED\nSTATUS: ${currentOutput.status.getOrNull()}\nDATA: ${currentOutput.data.getOrNull()}")
            }
        }
    }

    fun tryUpdate() {
        lastOutput.tryEmit(null)
        needToUpdate.tryEmit(false)
        needToUpdate.tryEmit(true)

        componentTransmitter
            .callbackMessenger.value
            ?.updateSubscriberCall(this)
    }

    override fun onOutput(subscribitionOutput: SubscribitionOutput) {
        super.onOutput(subscribitionOutput)
        lastOutput.tryEmit(subscribitionOutput)
    }

    @CallSuper
    open fun onOutputUpdate(
        data: Bundle?
    ) {
        if(data != null) needToUpdate.tryEmit(false)
    }

    override fun close() {
        super.close()
        updating!!.cancel()
    }
}