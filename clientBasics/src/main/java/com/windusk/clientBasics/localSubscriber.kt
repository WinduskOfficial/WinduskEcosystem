package com.windusk.clientBasics

import android.content.ComponentName
import com.windusk.clientBasics.data.sharings.saver.SharingSaver
import com.windusk.componentBasics.sharing.delegate.SubscriberDelegate
import com.windusk.clientBasics.domain.FullSharingAssociation
import com.windusk.clientBasics.domain.FullSharingAssociation.Companion.toFullAssociation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
fun SharingSaver.getSharingFlow(
    association: FullSharingAssociation,
    includeDisabled: Boolean = true
) = getSavedFlow().map { map ->
    map.filter { includeDisabled || it.value.enabled.get() }.toList()
        .firstOrNull { it.first == association }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T>SharingSaver.getSharingDelegatedFlow(
    name: ComponentName?,
    delegate: SubscriberDelegate<T>,
    key: String,
    filterDisabled: Boolean = true
) = getSharingFlow(
    delegate.getSubscribition().toFullAssociation(name, key),
    filterDisabled
).map {
    delegate.formatData(it?.second?.data?.get())
}