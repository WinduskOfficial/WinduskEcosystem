package com.windusk.clientBasics.data.sharings.search

import com.windusk.clientBasics.domain.FullSharingAssociation
import com.windusk.clientBasics.data.sharings.helper.MultiSubscriberHelper
import com.windusk.ecosystem.subscribition.Subscribition.Companion.canMatchWith

class MultiSharingSearch(
    private val subscriber: MultiSubscriberHelper,
    private val context: SearchContext
) {
    private val currentSaved = context.sharingSaver.getSaved()

    val matched by lazy {
        currentSaved.keys.filter {
            subscriber.subscribition.canMatchWith(it.toAssociation())
        }.toSet()
    }

    fun predictIsDefined(
        sharingAssociation: FullSharingAssociation
    ) = subscriber.subscribition.canMatchWith(sharingAssociation.toAssociation())
}