package com.windusk.clientBasics.data.sharings.saver

import android.content.ComponentName
import com.windusk.clientBasics.component.Component
import com.windusk.clientBasics.data.sharings.helper.MultiSubscriberHelper
import com.windusk.clientBasics.data.sharings.helper.PrioritySubscriberHelper
import com.windusk.ecosystem.sharing.subscriber.PrioritySharingSubscriber
import com.windusk.ecosystem.sharing.subscriber.SharingMultiSubscriber
import com.windusk.ecosystem.subscribition.MultiSubscribition
import com.windusk.ecosystem.subscribition.PrioritySubscribition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class SubscriberSaver {
    private val idGenerator = AtomicLong(0)

    private val priorityStorage = ConcurrentHashMap<Long, PrioritySubscriberHelper>()
    private val multiStorage = ConcurrentHashMap<Long, MultiSubscriberHelper>()

    private val priority = MutableStateFlow<List<PrioritySubscriberHelper>>(emptyList())
    private val multi = MutableStateFlow<List<MultiSubscriberHelper>>(emptyList())

    fun getPriorityFlow() = priority.asStateFlow()
    fun getMultiFlow() = multi.asStateFlow()

    fun getPriority() = priority.value
    fun getMulti() = multi.value

    fun onPrioritySubscribed(
        component: Component?,
        subscriber: PrioritySharingSubscriber,
        subscribition: PrioritySubscribition
    ): PrioritySubscriberHelper {
        val helper = PrioritySubscriberHelper(component, subscriber, subscribition)

        priority {
            put(idGenerator.incrementAndGet(), helper)
        }

        return helper
    }

    fun onMultiSubscribed(
        component: Component?,
        subscriber: SharingMultiSubscriber,
        subscribition: MultiSubscribition
    ): MultiSubscriberHelper {
        val helper = MultiSubscriberHelper(component, subscriber, subscribition)

        multi {
            put(idGenerator.incrementAndGet(), helper)
        }

        return helper
    }

    fun onPriorityUnsubscribed(
        subscriber: PrioritySharingSubscriber
    ) {
        priority {
            val iterator = priorityStorage.iterator()
            while (iterator.hasNext()) {
                if(
                    iterator.next().value.subscribersEquals(subscriber)
                ) iterator.remove()
            }
        }
    }

    fun onMultiUnsubscribed(
        subscriber: SharingMultiSubscriber
    ) {
        multi {
            val iterator = multiStorage.iterator()
            while (iterator.hasNext()) {
                if(
                    iterator.next().value.subscribersEquals(subscriber)
                ) iterator.remove()
            }
        }
    }

    fun onComponentDead(
        name: ComponentName
    ) {
        priority {
            val iterator = priorityStorage.iterator()
            while (iterator.hasNext()) {
                val (_, subscriber) = iterator.next()
                if(subscriber.componentName != null && subscriber.componentName == name) iterator.remove()
            }
        }

        multi {
            val iterator = multiStorage.iterator()
            while (iterator.hasNext()) {
                val (_, subscriber) = iterator.next()
                if(subscriber.componentName != null && subscriber.componentName == name) {
                    subscriber.close()
                    iterator.remove()
                }
            }
        }
    }

    private fun priority(
        action: ConcurrentHashMap<Long, PrioritySubscriberHelper>.() -> Unit
    ) {
        priorityStorage.action()
        priority.tryEmit(priorityStorage.values.toList())
    }

    private fun multi(
        action: ConcurrentHashMap<Long, MultiSubscriberHelper>.() -> Unit
    ) {
        multiStorage.action()
        multi.tryEmit(multiStorage.values.toList())
    }

    private fun priorityDead(
        whiteListComponentNames: List<ComponentName>,
        subscriber: MultiSubscriberHelper
    ) = subscriber.componentName in whiteListComponentNames + null

    private fun multiDead(
        whiteListComponentNames: List<ComponentName>,
        subscriber: MultiSubscriberHelper
    ) = subscriber.componentName in whiteListComponentNames + null
}