package com.windusk.clientBasics.data.prioritySharings.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.windusk.clientBasics.data.prioritySharings.database.PrioritySharing.Companion.COLUMN_DATA_TYPE
import com.windusk.clientBasics.data.prioritySharings.database.PrioritySharing.Companion.COLUMN_TAG
import com.windusk.clientBasics.data.prioritySharings.database.PrioritySharing.Companion.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface PrioritySharingDao {
    @Query("SELECT * FROM $TABLE_NAME")
    fun getAllFlow(): Flow<List<PrioritySharing>>

    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getAll(): List<PrioritySharing>

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_DATA_TYPE = :dataType AND $COLUMN_TAG = :tag")
    suspend fun findByDataTypeAndTag(dataType: String, tag: String): PrioritySharing?

    @Insert
    suspend fun insert(association: PrioritySharing)

    @Update
    suspend fun update(association: PrioritySharing)

    @Delete
    suspend fun delete(association: PrioritySharing)

    @Query("DELETE FROM $TABLE_NAME WHERE $COLUMN_DATA_TYPE = :dataType AND $COLUMN_TAG = :tag")
    suspend fun deleteByDataTypeAndTag(dataType: String, tag: String): Int

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()
}