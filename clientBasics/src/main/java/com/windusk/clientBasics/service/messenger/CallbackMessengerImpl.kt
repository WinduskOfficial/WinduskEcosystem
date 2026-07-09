package com.windusk.clientBasics.service.messenger

import android.os.Bundle
import android.util.Log
import com.windusk.ecosystem.CallbackMessenger
import com.windusk.clientBasics.data.factory.EcosystemFactory
import com.windusk.clientBasics.component.Component
import com.windusk.ecosystem.sharing.SharingAssociation
import com.windusk.ecosystem.sharing.subscriber.SharingMultiSubscriber
import com.windusk.ecosystem.sharing.subscriber.PrioritySharingSubscriber
import com.windusk.ecosystem.subscribition.MultiSubscribition
import com.windusk.ecosystem.subscribition.PrioritySubscribition

class CallbackMessengerImpl(
    private val component: Component,
    private val ecosystemFactory: EcosystemFactory
): CallbackMessenger.Stub() {
    override fun updateSharing(
        association: SharingAssociation,
        sharingOutput: Bundle
    ) {
        component.runIfState(
            Component.State.ENABLED,
            Component.State.DISABLED,
            Component.State.DISABLED,
            blockName = "updateSharing"
        ) {
            ecosystemFactory.sharingFactory.updateSharing(component, association, sharingOutput)
        }
    }

    override fun subscribeToSharing(
        subscriber: PrioritySharingSubscriber,
        subscribition: Bundle
    ) {
        component.runIfState(
            Component.State.ENABLED,
            Component.State.DISABLED,
            blockName = "subscribeToSharing"
        ) {

            ecosystemFactory.sharingFactory.addSubscriber(
                component,
                subscriber,
                PrioritySubscribition.import(subscribition)
            )
        }
    }

    override fun unsubscribeFromSharing(
        subscriber: PrioritySharingSubscriber
    ) {
        component.runIfState(
            Component.State.DISABLED,
            Component.State.ENABLED,
            blockName = "unsubscribeFromSharing"
        ) {
            ecosystemFactory.sharingFactory.removeSubscriber(subscriber)
        }
    }

    override fun multiSubscribeToSharing(
        subscriber: SharingMultiSubscriber,
        subscribition: Bundle
    ) {
        Log.d("", "multiSubscribeToSharing")
        component.runIfState(
            Component.State.ENABLED,
            blockName = "multiSubscribeToSharing"
        ) {
            ecosystemFactory.sharingFactory.addMultiSubscriber(
                component,
                subscriber,
                MultiSubscribition.import(subscribition)
            )
        }
    }

    override fun multiUnsubscribeFromSharing(
        subscriber: SharingMultiSubscriber
    ) {
        Log.d("", "multiUnsubscribeFromSharing")
        component.runIfState(
            Component.State.DISABLED,
            Component.State.ENABLED,
            blockName = "multiUnsubscribeFromSharing"
        ) {
            ecosystemFactory.sharingFactory.removeMultiSubscriber(subscriber)
        }
    }

    override fun updateSubscriberCall(
        subscriber: PrioritySharingSubscriber
    ) {
        Log.d("", "updateSubscriberCall")
        component.runIfState(
            Component.State.ENABLED,
            blockName = "updateSubscriberCall"
        ) {
            ecosystemFactory.sharingFactory.processSubscriberCallback(subscriber)
        }
    }
}