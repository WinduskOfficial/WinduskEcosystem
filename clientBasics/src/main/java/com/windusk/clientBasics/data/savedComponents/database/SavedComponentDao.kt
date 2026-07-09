package com.windusk.clientBasics.data.savedComponents.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedComponentDao {
    @Query("SELECT * FROM ${SavedComponent.TABLE_NAME}")
    fun getAllFlow(): Flow<List<SavedComponent>>

    @Query("SELECT * FROM ${SavedComponent.TABLE_NAME}")
    suspend fun getAll(): List<SavedComponent>

    @Query("SELECT * FROM ${SavedComponent.TABLE_NAME} WHERE ${SavedComponent.COLUMN_ALLOWED} = 1")
    suspend fun getAllowed(): List<SavedComponent>

    @Query("SELECT * FROM ${SavedComponent.TABLE_NAME} WHERE ${SavedComponent.COLUMN_PACKAGE} = :packageName AND ${SavedComponent.COLUMN_CLASS} = :className")
    fun getByNameFlow(packageName: String, className: String): Flow<SavedComponent?>

    @Query("SELECT * FROM ${SavedComponent.TABLE_NAME} WHERE ${SavedComponent.COLUMN_PACKAGE} = :packageName AND ${SavedComponent.COLUMN_CLASS} = :className")
    suspend fun getByName(packageName: String, className: String): SavedComponent?

    @Query("UPDATE ${SavedComponent.TABLE_NAME} SET ${SavedComponent.COLUMN_ALLOWED} = :allowed WHERE ${SavedComponent.COLUMN_PACKAGE} = :packageName AND ${SavedComponent.COLUMN_CLASS} = :className")
    suspend fun updateAllowed(packageName: String, className: String, allowed: Boolean)

    @Query("UPDATE ${SavedComponent.TABLE_NAME} SET ${SavedComponent.COLUMN_ENABLED} = :enabled WHERE ${SavedComponent.COLUMN_PACKAGE} = :packageName AND ${SavedComponent.COLUMN_CLASS} = :className")
    suspend fun updateEnabled(packageName: String, className: String, enabled: Boolean)

    @Query("UPDATE ${SavedComponent.TABLE_NAME} SET ${SavedComponent.COLUMN_ALLOWED} = :allowed, ${SavedComponent.COLUMN_ENABLED} = :enabled WHERE ${SavedComponent.COLUMN_PACKAGE} = :packageName AND ${SavedComponent.COLUMN_CLASS} = :className")
    suspend fun update(packageName: String, className: String, allowed: Boolean, enabled: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(registration: SavedComponent)

    @Delete
    suspend fun delete(registration: SavedComponent)

    @Query("DELETE FROM ${SavedComponent.TABLE_NAME}")
    suspend fun deleteAll()
}