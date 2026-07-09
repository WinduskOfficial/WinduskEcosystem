package com.windusk.clientBasics.data.prioritySharings.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrioritySharingModule {
    @Provides
    fun providePrioritySharingDao(database: PrioritySharingDatabase) = database.prioritySharingDao()

    @Provides
    @Singleton
    fun providePrioritySharingDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        PrioritySharingDatabase::class.java,
        "ecosystem_priority_sharing.db"
    ).build()
}