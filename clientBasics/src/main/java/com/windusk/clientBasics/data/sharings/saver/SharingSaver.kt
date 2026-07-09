package com.windusk.clientBasics.data.sharings.saver

import android.content.ComponentName
import com.windusk.clientBasics.domain.FullSharingAssociation
import com.windusk.ecosystem.sharing.SharingOutput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.ConcurrentHashMap

class SharingSaver {
    private val savedStorage = ConcurrentHashMap<FullSharingAssociation, SharingOutput>()
    private val saved = MutableStateFlow<Map<FullSharingAssociation, SharingOutput>>(mapOf())

    fun getSaved() = saved.value
    fun getSavedFlow() = saved.asStateFlow()

    fun onUpdate(
        sharingAssociation: FullSharingAssociation,
        output: SharingOutput
    ) {
        saved {
            put(sharingAssociation, output)
        }
    }

    fun onComponentDead(
        name: ComponentName
    ) {
        filterSaved {
            it.first.isStandart() || it.first.getComponentName() != name
        }
    }

    private fun filterSaved(
        predicate: (Pair<FullSharingAssociation, SharingOutput>) -> Boolean
    ) {
        saved {
            val iterator = savedStorage.iterator()

            while (iterator.hasNext()) {
                val (key, value) = iterator.next()
                if(!predicate(key to value)) iterator.remove()
            }
        }
    }

    private fun saved(
        action: ConcurrentHashMap<FullSharingAssociation, SharingOutput>.() -> Unit
    ) {
        savedStorage.action()
        saved.tryEmit(savedStorage.toMap())
    }
}