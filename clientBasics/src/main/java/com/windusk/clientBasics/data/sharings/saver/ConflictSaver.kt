package com.windusk.clientBasics.data.sharings.saver

import android.content.ComponentName
import com.windusk.clientBasics.domain.FullSharingAssociation
import com.windusk.clientBasics.data.sharings.helper.PrioritySubscriberHelper
import com.windusk.ecosystem.subscribition.Subscribition.Companion.canMatchWith
import java.util.concurrent.ConcurrentHashMap

class ConflictSaver(
    private val onChoiced: (Set<PrioritySubscriberHelper>) -> Unit
) {
    private val conflicts = ConcurrentHashMap<PrioritySubscriberHelper, FullSharingAssociation?>()

    fun onSubscribersUpdated(
        currentSubscribers: Set<PrioritySubscriberHelper>
    ) {
        val identitySet = currentSubscribers.map { System.identityHashCode(it) }.toSet()

        filterConflicts { (subscriber, _) ->
            System.identityHashCode(subscriber) in identitySet
        }
    }

    fun onSubscriberRemoved(
        deadSubscriber: PrioritySubscriberHelper
    ) {
        filterConflicts { it.first === deadSubscriber }
    }

    fun onConflict(
        savedSharings: Set<FullSharingAssociation>,
        subscriber: PrioritySubscriberHelper
    ): FullSharingAssociation? {
        val choiced = conflicts.putIfAbsent(subscriber, null) ?: return null

        val supportPriority = subscriber.subscribition.supportPriority.get()
        val choicedDead = !savedSharings.contains(choiced)

        if(choicedDead) {
            conflicts[subscriber] = null
            return null
        }

        if(!supportPriority) conflicts.remove(subscriber)

        return choiced
    }

    fun onChoiced(
        association: FullSharingAssociation
    ) {
        val matches = mutableSetOf<PrioritySubscriberHelper>()

        conflicts.replaceAll { subscriber, choiced ->
            if(choiced != null) return@replaceAll choiced

            val canMatch = subscriber.subscribition.canMatchWith(association.toAssociation())
            if(!canMatch) return@replaceAll choiced

            matches += subscriber
            return@replaceAll association
        }

        onChoiced(matches)
    }

    fun onPrioritySaved(
        sharingAssociation: FullSharingAssociation
    ) {
        filterConflicts { (subscriber, _) ->
            !subscriber.subscribition.canMatchWith(sharingAssociation.toAssociation())
        }
    }

    fun onComponentDead(
        componentName: ComponentName
    ) {
        filterConflicts { (subscriber, _) ->
            subscriber.componentName == componentName
        }

        conflicts.replaceAll { _, association ->
            association?.takeIf { it.getComponentName() == componentName }
        }
    }

    private fun filterConflicts(
        predicate: (Pair<PrioritySubscriberHelper, FullSharingAssociation?>) -> Boolean
    ) {
        val iterator = conflicts.entries.iterator()

        while (iterator.hasNext()) {
            val (key, value) = iterator.next()
            if(!predicate(key to value)) iterator.remove()
        }
    }
}