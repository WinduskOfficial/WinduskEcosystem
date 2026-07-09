package com.windusk.clientBasics.data.sharings.saver

import com.windusk.clientBasics.data.prioritySharings.PrioritySharingRepository
import com.windusk.clientBasics.domain.FullSharingAssociation

class PrioritySaver(
    private val repository: PrioritySharingRepository,
    private val onSaved: (FullSharingAssociation) -> Unit
) {
    suspend fun onSave(sharingAssociation: FullSharingAssociation) {
        repository.upsert(sharingAssociation)
        onSaved(sharingAssociation)
    }

    suspend fun get(dataType: String, tag: String) =
        repository.getByDataTypeAndTag(dataType, tag)

    suspend fun forget(dataType: String, tag: String) {
        repository.deleteByDataTypeAndTag(dataType, tag)
    }
}