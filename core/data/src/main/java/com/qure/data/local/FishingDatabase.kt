package com.qure.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.qure.data.entity.fishingspot.FishingSpotBookmarkEntity
import com.qure.data.entity.fishingspot.FishingSpotLocalEntity
import com.qure.data.entity.memo.MemoLocalEntity
import com.qure.data.entity.weather.WeatherLocalEntity
import com.qure.data.utils.Converters

@Database(
    entities = [FishingSpotBookmarkEntity::class, MemoLocalEntity::class, WeatherLocalEntity::class, FishingSpotLocalEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
internal abstract class FishingDatabase : RoomDatabase() {
    abstract fun fishingSpotBookmarkDao(): FishingBookmarkDao
    abstract fun memoDao(): MemoDao
    abstract fun weatherDao(): WeatherDao
    abstract fun fishingSpotDao(): FishingSpotDao
}
