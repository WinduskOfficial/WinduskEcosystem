package com.windusk.componentBasics.sharing.subscriber

import android.os.Bundle
import androidx.annotation.CallSuper
import com.windusk.componentBasics.LazyComponentTransmitter
import com.windusk.componentBasics.sharing.delegate.SubscriberDelegate
import com.windusk.ecosystem.CallbackMessenger
import com.windusk.ecosystem.logger.EcosystemLogger.Companion.collectLatestWithLog
import com.windusk.ecosystem.logger.LogInterface
import com.windusk.ecosystem.logger.LogOutput
import com.windusk.ecosystem.subscribition.SubscribitionOutput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class ComponentSubscriber<T>(
    private val scope: CoroutineScope,
    private val componentTransmitter: LazyComponentTransmitter<*>
): BasicSubscriber<T>() {
    companion object {
        fun <T>create(
            scope: CoroutineScope,
            componentTransmitter: LazyComponentTransmitter<*>,
            delegate: SubscriberDelegate<T>
        ) = ComponentSubscriber<T>(scope, componentTransmitter).apply { this.delegate = delegate }
    }

    private var subscribing: Job? = null

    override fun onUpdate(subscribitionOutput: Bundle) {
        val output = SubscribitionOutput.import(subscribitionOutput)
        onOutput(output)
    }

    override fun sendUpdateCall() {
        componentTransmitter.callbackMessenger.value
            ?.updateSubscriberCall(this)
    }

    @CallSuper
    override fun init() {
        subscribing = scope.launch {
            var tempCallbackMessenger: CallbackMessenger? = null

            try {
                componentTransmitter.callbackMessenger.collectLatestWithLog(componentTransmitter.logger, "PRIORITY SUBSCRIBER (TYPE: ${subscribition.dataType.get()}, TAG: ${subscribition.tag.get()})") {
                    tempCallbackMessenger?.unsubscribeFromSharing(this@ComponentSubscriber)
                    tempCallbackMessenger = it

                    tempCallbackMessenger
                        ?.subscribeToSharing(
                            this@ComponentSubscriber,
                            subscribition.export()
                        )

                    LogOutput(LogInterface.INFO, "SUBSCRIBER RESUBSCRIBED TO $tempCallbackMessenger")
                }
            }
            catch(e: Exception) {
                componentTransmitter.logger.send(
                    LogOutput(LogInterface.ERROR, "ERROR WHILE RESUBSCRIBING\n\n${e.cause}\n\nPRIORITY SUBSCRIBER (TYPE: ${subscribition.dataType.get()}, TAG: ${subscribition.tag.get()})\nLAST MESSENGER: $tempCallbackMessenger")
                )
            }
            finally {
                try {
                    tempCallbackMessenger?.unsubscribeFromSharing(this@ComponentSubscriber)
                } catch(e: Exception) {
                    componentTransmitter.logger.send(
                        LogOutput(LogInterface.ERROR, "ERROR WHILE UNSUBSCRIBING\n\n${e.cause}\n\nPRIORITY SUBSCRIBER (TYPE: ${subscribition.dataType.get()}, TAG: ${subscribition.tag.get()})\nLAST MESSENGER: $tempCallbackMessenger")
                    )
                }
            }
        }
    }

    @CallSuper
    override fun close() {
        subscribing!!.cancel()
        subscribing = null
    }
}