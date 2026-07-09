package com.windusk.componentBasics.sharing.multiSubscriber

import android.os.Bundle
import com.windusk.ecosystem.sharing.subscriber.PrioritySharingSubscriber
import com.windusk.ecosystem.subscribition.SubscribitionOutput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class SubscriberForMultiSubscriber(): PrioritySharingSubscriber.Stub() {
    val output = MutableStateFlow<SubscribitionOutput?>(null)
    val data = output.map { it?.data?.getOrNull() }

    override fun onUpdate(subscribitionOutput: Bundle) {
        output.tryEmit(SubscribitionOutput.import(subscribitionOutput))
    }
}