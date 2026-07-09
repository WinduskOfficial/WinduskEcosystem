package com.windusk.componentBasics.sharing.multiSubscriber

import androidx.annotation.CallSuper
import com.windusk.componentBasics.LazyComponentTransmitter
import com.windusk.componentBasics.sharing.delegate.SubscriberDelegate
import com.windusk.ecosystem.CallbackMessenger
import com.windusk.ecosystem.logger.EcosystemLogger.Companion.collectLatestWithLog
import com.windusk.ecosystem.logger.LogInterface
import com.windusk.ecosystem.logger.LogOutput
import com.windusk.ecosystem.sharing.subscriber.PrioritySharingSubscriber
import com.windusk.ecosystem.subscribition.MultiSubscribition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ComponentMultiSubscriber<T>(
    private val scope: CoroutineScope,
    private val componentTransmitter: LazyComponentTransmitter<*>,
): BasicMultiSubscriber<T>() {
    companion object {
        fun <T>create(
            scope: CoroutineScope,
            componentTransmitter: LazyComponentTransmitter<*>,
            delegate: SubscriberDelegate<T>
        ) = ComponentMultiSubscriber<T>(scope, componentTransmitter).apply { this.delegate = delegate }
    }

    private val subscribersMutable = MutableStateFlow<List<SubscriberForMultiSubscriber>>(emptyList())
    private var subscribing: Job? = null

    @CallSuper
    override fun init() {
        val multiSubscribition = subscribition.exportAs(MultiSubscribition::class)

        subscribing = scope.launch {
            var tempCallbackMessenger: CallbackMessenger? = null

            try {
                componentTransmitter.callbackMessenger.collectLatestWithLog(componentTransmitter.logger, "MULTI SUBSCRIBER (TYPE: ${multiSubscribition.dataType.get()}, TAG: ${multiSubscribition.tag.get()})") {
                    tempCallbackMessenger?.multiUnsubscribeFromSharing(this@ComponentMultiSubscriber)
                    tempCallbackMessenger = it

                    tempCallbackMessenger
                        ?.multiSubscribeToSharing(
                            this@ComponentMultiSubscriber,
                            multiSubscribition.export()
                        )

                    LogOutput(LogInterface.INFO, "SUBSCRIBER RESUBSCRIBED TO $tempCallbackMessenger")
                }
            }
            catch(e: NullPointerException) {
                componentTransmitter.logger.send(
                    LogOutput(LogInterface.ERROR, "ERROR WHILE RESUBSCRIBING\n\n${e.cause}\n\nMULTI SUBSCRIBER (TYPE: ${multiSubscribition.dataType.get()}, TAG: ${multiSubscribition.tag.get()})\nLAST MESSENGER: $tempCallbackMessenger")
                )
            }
            finally {
                try {
                    tempCallbackMessenger?.multiUnsubscribeFromSharing(this@ComponentMultiSubscriber)
                } catch(e: Exception) {
                    componentTransmitter.logger.send(
                        LogOutput(LogInterface.ERROR, "ERROR WHILE UNSUBSCRIBING\n\n${e.cause}\n\nPRIORITY SUBSCRIBER (TYPE: ${multiSubscribition.dataType.get()}, TAG: ${multiSubscribition.tag.get()})\nLAST MESSENGER: $tempCallbackMessenger")
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

    override fun killSubscriber(subscriber: PrioritySharingSubscriber?) {
        subscribersMutable.update { oldSubscribers ->
            oldSubscribers.filter { it != subscriber }
        }
    }
}