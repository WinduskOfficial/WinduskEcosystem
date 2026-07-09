package com.windusk.clientBasics.data.savedComponents.database

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
object SavedComponentModule {
    @Provides
    fun provideSavedComponentDao(database: SavedComponentDatabase) = database.componentDao()

    @Provides
    @Singleton
    fun provideSavedComponentDatabase(@ApplicationContext context: Context)= Room.databaseBuilder(
        context,
        SavedComponentDatabase::class.java,
        "ecosystem_components.db"
    ).build()
}