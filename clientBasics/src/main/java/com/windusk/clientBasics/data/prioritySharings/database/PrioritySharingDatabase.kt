package com.windusk.clientBasics.data.prioritySharings.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PrioritySharing::class],
    version = 1,
    exportSchema = false
)
abstract class PrioritySharingDatabase : RoomDatabase() {
    abstract fun prioritySharingDao(): PrioritySharingDao
}