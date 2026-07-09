package com.windusk.clientBasics.data.sharings.search

import com.windusk.clientBasics.data.prioritySharings.database.PrioritySharing.Companion.toAssociation
import com.windusk.clientBasics.domain.FullSharingAssociation
import com.windusk.clientBasics.data.sharings.helper.PrioritySubscriberHelper
import com.windusk.ecosystem.subscribition.Subscribition.Companion.canMatchWith
import com.windusk.ecosystem.subscribition.SubscribitionOutput
import kotlinx.coroutines.runBlocking
import kotlin.collections.contains
import kotlin.collections.get

class PrioritySharingSearch(
    private val subscriber: PrioritySubscriberHelper,
    private val context: SearchContext
) {
    private val currentSaved = context.sharingSaver.getSaved()

    val matched by lazy {
        currentSaved.keys.filter {
            subscriber.subscribition.canMatchWith(it.toAssociation())
        }.toSet()
    }

    val priority by lazy {
        runBlocking {
            if (matched.isEmpty()) return@runBlocking null
            if (matched.size == 1) return@runBlocking matched.toList().first()

            context.prioritySaver.get(
                subscriber.subscribition.dataType.get(),
                subscriber.subscribition.tag.get()
            )?.toAssociation()
                .takeIf { subscriber.subscribition.supportPriority.get() }
        }
    }

    private val choiced by lazy {
        context.conflictSaver.onConflict(matched, subscriber)
    }

    fun generateOutputIfDefined(
        sharingAssociation: FullSharingAssociation,
        mustTakeChoised: Boolean = false
    ): SubscribitionOutput? {
        val defined = getDefined(mustTakeChoised, matched + sharingAssociation)
            .takeIf { it.second != sharingAssociation } ?: return null

        return SubscribitionOutput.new().apply {
            status
                .set(defined.first)

            currentSaved[defined.second]?.data
                ?.getOrNull()
                ?.let { data.set(it) }
        }
    }

    fun generateOutput(
        mustTakeChoised: Boolean = false,
        matchedProviders: Set<FullSharingAssociation> = matched
    ): SubscribitionOutput {
        val defined = getDefined(mustTakeChoised, matchedProviders)

        return SubscribitionOutput.new().apply {
            status
                .set(defined.first)

            currentSaved[defined.second]?.data
                ?.getOrNull()
                ?.let { data.set(it) }
        }
    }

    fun predictIsDefined(
        sharingAssociation: FullSharingAssociation,
        mustTakeChoised: Boolean = false
    ) = predictStatusFor(sharingAssociation, mustTakeChoised) in listOf(
        SubscribitionOutput.SharingStatus.PRIORITY_SHARING,
        SubscribitionOutput.SharingStatus.CHOISED_SHARING
    )

    fun predictStatusFor(
        sharingAssociation: FullSharingAssociation,
        mustTakeChoised: Boolean = false
    ): SubscribitionOutput.SharingStatus? {
        val canMatch = subscriber.subscribition.canMatchWith(sharingAssociation.toAssociation())
        if(!canMatch) return null

        val defined = getDefined(mustTakeChoised, matched + sharingAssociation)
            .takeIf { it.second == sharingAssociation }

        return defined?.first
    }

    fun getDefined(
        mustTakeChoised: Boolean = false,
        matchedProviders: Set<FullSharingAssociation> = matched
    ) = when {
        matchedProviders.isEmpty() ->
            SubscribitionOutput.SharingStatus.HAVENT_SHARING to null

        matchedProviders.size == 1 ->
            SubscribitionOutput.SharingStatus.PRIORITY_SHARING to matchedProviders.first()

        priority
            .takeIf { it in matchedProviders } != null ->
                SubscribitionOutput.SharingStatus.PRIORITY_SHARING to priority

        choiced
            .takeIf { it in matchedProviders }
            .takeIf { !subscriber.subscribition.updateManually.get() || mustTakeChoised } != null ->
                SubscribitionOutput.SharingStatus.CHOISED_SHARING to choiced

        else ->
            SubscribitionOutput.SharingStatus.NEED_CALLBACK to null
    }.let { it.first to it.second }

    fun getChoiced(
        mustTakeChoised: Boolean = false
    ) = choiced.takeIf { !subscriber.subscribition.updateManually.get() || mustTakeChoised }
}