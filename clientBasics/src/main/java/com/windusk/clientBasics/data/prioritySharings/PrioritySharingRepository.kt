package com.windusk.clientBasics.data.prioritySharings

import com.windusk.clientBasics.data.prioritySharings.database.PrioritySharing
import com.windusk.clientBasics.data.prioritySharings.database.PrioritySharingDao
import com.windusk.clientBasics.domain.FullSharingAssociation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrioritySharingRepository @Inject constructor(
    private val dao: PrioritySharingDao
) {
    val all = dao.getAllFlow()

    suspend fun getAll(): List<PrioritySharing> = dao.getAll()

    suspend fun getByDataTypeAndTag(dataType: String, tag: String): PrioritySharing? {
        return dao.findByDataTypeAndTag(dataType, tag)
    }

    suspend fun delete(association: PrioritySharing) {
        dao.delete(association)
    }

    suspend fun deleteByDataTypeAndTag(dataType: String, tag: String) {
        dao.deleteByDataTypeAndTag(dataType, tag)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    suspend fun upsert(sharingAssociation: FullSharingAssociation) {
        val prioritySharing = PrioritySharing.fromAssociation(sharingAssociation)
        val existing = dao.findByDataTypeAndTag(
            prioritySharing.dataType,
            prioritySharing.tag
        )

        if (existing != null)
            dao.update(prioritySharing)
        else
            dao.insert(prioritySharing)
    }

    suspend fun upsertPriority(
        dataType: String,
        tag: String,
        priority: FullSharingAssociation
    ): Boolean {
        return try {
            unsafetyUpsertPriority(dataType, tag, priority)
            true
        } catch (_: Exception) {
            false
        }
    }

    private suspend fun unsafetyUpsertPriority(
        dataType: String,
        tag: String,
        priority: FullSharingAssociation
    ) {
        val prioritySharing = PrioritySharing.fromAssociation(priority)
        val existing = getByDataTypeAndTag(dataType, tag)

        if(existing != null)
            dao.update(prioritySharing)
        else
            dao.insert(prioritySharing)
    }
}
