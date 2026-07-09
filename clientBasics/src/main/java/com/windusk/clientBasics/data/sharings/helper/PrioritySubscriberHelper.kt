package com.windusk.clientBasics.data.sharings.helper

import com.windusk.clientBasics.component.Component
import com.windusk.ecosystem.sharing.subscriber.PrioritySharingSubscriber
import com.windusk.ecosystem.subscribition.PrioritySubscribition
import com.windusk.ecosystem.subscribition.SubscribitionOutput

class PrioritySubscriberHelper(
    private val component: Component?,
    private val subscriber: PrioritySharingSubscriber,
    val subscribition: PrioritySubscribition
) {
    val componentName = component?.name

    fun tryUpdate(subscribitionOutput: SubscribitionOutput) {
        try {
            subscriber.onUpdate(subscribitionOutput.export())
        }
        catch (e: Exception) {
            component?.crash("Получение данных от компонента", e)
        }
    }

    fun subscribersEquals(otherSubscriber: PrioritySharingSubscriber) = subscriber.asBinder() == otherSubscriber.asBinder()
}