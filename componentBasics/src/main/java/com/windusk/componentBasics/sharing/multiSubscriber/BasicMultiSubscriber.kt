package com.windusk.componentBasics.sharing.multiSubscriber

import androidx.annotation.CallSuper
import com.windusk.componentBasics.sharing.delegate.SubscriberDelegate
import com.windusk.ecosystem.sharing.subscriber.SharingMultiSubscriber
import com.windusk.ecosystem.sharing.subscriber.PrioritySharingSubscriber
import com.windusk.ecosystem.subscribition.MultiSubscribition
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

abstract class BasicMultiSubscriber<T>: SharingMultiSubscriber.Stub() {
    protected lateinit var delegate: SubscriberDelegate<T>

    private val subscribersMutable = MutableStateFlow<List<SubscriberForMultiSubscriber>>(emptyList())
    val subscribers = subscribersMutable.asStateFlow()

    val subscribition by lazy {
        delegate.getSubscribition().exportAs(MultiSubscribition::class)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val outputs = subscribers
        .flatMapLatest { list ->
            val flows = list.map { it.data }
            combine(flows + flowOf(null)) { it.toList().filterNotNull() }
        }

    val data = outputs.map { list ->
        list.map { delegate.formatData(it) }
    }

    abstract fun init()

    abstract fun close()

    @CallSuper
    override fun generateNewSubscriber(): PrioritySharingSubscriber = SubscriberForMultiSubscriber().apply {
        subscribersMutable.update { it + this }
    }

    override fun killSubscriber(subscriber: PrioritySharingSubscriber?) {
        subscribersMutable.update { oldSubscribers ->
            oldSubscribers.filter { it != subscriber }
        }
    }
}