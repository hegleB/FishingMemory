package com.qure.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.qure.data.entity.fishingspot.FishingSpotBookmarkEntity

@Dao
internal interface FishingBookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFishingSpot(fishingSpotBookmarkEntity: FishingSpotBookmarkEntity)

    @Query("select * from fishing_spot_bookmark_table")
    suspend fun getFishingSpots(): List<FishingSpotBookmarkEntity>

    @Query("select EXISTS(SELECT * from fishing_spot_bookmark_table where number = :number)")
    suspend fun checkFishingSpot(number: Int): Boolean

    @Delete
    suspend fun deleteFishingSpot(fishingSpotBookmarkEntity: FishingSpotBookmarkEntity)

    @Query("delete from fishing_spot_bookmark_table")
    suspend fun deleteAllFishingSpots()
}
