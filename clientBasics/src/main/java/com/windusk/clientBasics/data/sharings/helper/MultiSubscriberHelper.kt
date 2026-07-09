package com.windusk.clientBasics.data.sharings.helper

import android.content.ComponentName
import com.windusk.clientBasics.component.Component
import com.windusk.clientBasics.domain.FullSharingAssociation
import com.windusk.ecosystem.sharing.SharingOutput
import com.windusk.ecosystem.sharing.subscriber.PrioritySharingSubscriber
import com.windusk.ecosystem.sharing.subscriber.SharingMultiSubscriber
import com.windusk.ecosystem.subscribition.MultiSubscribition
import com.windusk.ecosystem.subscribition.Subscribition.Companion.canMatchWith
import com.windusk.ecosystem.subscribition.SubscribitionOutput
import java.util.concurrent.ConcurrentHashMap

class MultiSubscriberHelper(
    private val component: Component?,
    private val multiSubscriber: SharingMultiSubscriber,
    val subscribition: MultiSubscribition
) {
    @Volatile
    private var closed = false

    val componentName = component?.name

    private val subscribers = ConcurrentHashMap<FullSharingAssociation, PrioritySharingSubscriber>()

    fun onComponentDead(
        name: ComponentName
    ) {
        val iterator = subscribers.entries.iterator()

        while (iterator.hasNext()) {
            val (key, subscriber) = iterator.next()
            if(key.getComponentName() == name) {
                multiSubscriber.killSubscriber(subscriber)
                iterator.remove()
            }
        }
    }

    fun close() {
        if (closed) return
        closed = true

        subscribers.values.forEach { subscriber ->
            try {
                multiSubscriber.killSubscriber(subscriber)
            } catch (_: Exception) {}
        }
        subscribers.clear()
    }

    fun tryUpdate(
        savedSharing: Pair<FullSharingAssociation, SharingOutput>
    ) {
        try {
            update(savedSharing.first, savedSharing.second)
        }
        catch (e: Exception) {
            component?.crash("Получение данных", e)
        }
    }

    private fun update(
        association: FullSharingAssociation,
        output: SharingOutput?
    ) {
        if(
            !subscribition.canMatchWith(association.toAssociation())
        ) return

        val output = SubscribitionOutput.new()
            .apply {
                output?.data?.get()?.let { data.set(it) }
            }

        val subscriber = subscribers
            .getOrPut(association) {
                generateNewSubscriber(association)
            }

        subscriber.onUpdate(output.export())
    }

    private fun generateNewSubscriber(
        sharingAssociation: FullSharingAssociation
    ) = multiSubscriber.generateNewSubscriber().apply {
        subscribers[sharingAssociation] = this
    }

    fun subscribersEquals(otherSubscriber: SharingMultiSubscriber) = multiSubscriber.asBinder() == otherSubscriber.asBinder()
}