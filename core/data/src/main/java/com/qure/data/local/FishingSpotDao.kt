package com.qure.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.qure.data.entity.fishingspot.FishingSpotLocalEntity

@Dao
internal interface FishingSpotDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFishingSpots(fishingSpot: List<FishingSpotLocalEntity>)

    @Query("select * from fishing_spot_table")
    suspend fun getFishingSpots(): List<FishingSpotLocalEntity>
}
