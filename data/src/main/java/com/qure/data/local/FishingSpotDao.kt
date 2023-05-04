package com.qure.data.local

import androidx.room.*
import com.qure.data.entity.fishingspot.FishingSpotBookmarkEntity

@Dao
interface FishingSpotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFishingSpot(fishingSpotBookmarkEntity: FishingSpotBookmarkEntity)

    @Query("select * from fishingspot_table")
    fun getFishingSpots(): List<FishingSpotBookmarkEntity>

    @Query("select * from fishingspot_table where fishingspot_table.number = :number")
    fun checkFishingSpot(number: Int): Boolean

    @Delete
    suspend fun deleteFishingSpot(fishingSpotBookmarkEntity: FishingSpotBookmarkEntity)

    @Query("delete from fishingspot_table")
    suspend fun deleteAllFishingSpots()
}