package com.qure.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.qure.data.entity.fishingspot.FishingSpotBookmarkEntity

@Database(entities = [FishingSpotBookmarkEntity::class], version = 1, exportSchema = false)
abstract class FishingSpotDatabase : RoomDatabase() {
    abstract fun fishingSpotkDao(): FishingSpotDao
}
