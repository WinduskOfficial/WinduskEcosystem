package com.windusk.componentBasics.sharing.subscriber

import android.os.Bundle
import androidx.annotation.CallSuper
import com.windusk.componentBasics.sharing.delegate.SubscriberDelegate
import com.windusk.ecosystem.sharing.subscriber.PrioritySharingSubscriber
import com.windusk.ecosystem.subscribition.PrioritySubscribition
import com.windusk.ecosystem.subscribition.SubscribitionOutput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

abstract class BasicSubscriber<T>: PrioritySharingSubscriber.Stub() {
    protected lateinit var delegate: SubscriberDelegate<T>

    val subscribition by lazy {
        delegate.getSubscribition().exportAs(PrioritySubscribition::class)
    }

    override fun onUpdate(subscribitionOutput: Bundle) {
        val output = SubscribitionOutput.import(subscribitionOutput)
        onOutput(output)
    }

    abstract fun sendUpdateCall()

    @CallSuper
    abstract fun init()

    @CallSuper
    abstract fun close()

    @CallSuper
    open fun onOutput(subscribitionOutput: SubscribitionOutput) {
        outputMutable.tryEmit(subscribitionOutput)
    }

    private val outputMutable = MutableStateFlow<SubscribitionOutput?>(null)
    private val output = outputMutable.asStateFlow()

    val status = output.map { it?.status?.get() }
    val data = output.map {
        delegate.formatData(it?.data?.getOrNull())
    }
}