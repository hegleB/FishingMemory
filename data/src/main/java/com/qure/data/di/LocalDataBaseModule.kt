package com.qure.data.di

import android.content.Context
import androidx.room.Room
import com.qure.data.local.FishingSpotDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataBaseModule {
    @Provides
    @Singleton
    fun providesFishingSpotDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        FishingSpotDatabase::class.java,
        "fishingspot_db"
    ).build()

    @Provides
    @Singleton
    fun providesFishingSpotDao(database: FishingSpotDatabase) = database.fishingSpotkDao()
}