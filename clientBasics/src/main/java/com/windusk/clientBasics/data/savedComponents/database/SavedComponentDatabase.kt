package com.windusk.clientBasics.data.savedComponents.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SavedComponent::class],
    version = 1,
    exportSchema = false
)
abstract class SavedComponentDatabase : RoomDatabase() {
    abstract fun componentDao(): SavedComponentDao
}