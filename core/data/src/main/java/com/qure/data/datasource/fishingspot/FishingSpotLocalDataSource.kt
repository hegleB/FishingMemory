package com.qure.data.datasource.fishingspot

import com.qure.data.entity.fishingspot.FishingSpotBookmarkEntity

interface FishingSpotLocalDataSource {
    suspend fun insertFishingSpot(fishingSpotBookmarkEntity: FishingSpotBookmarkEntity)

    suspend fun getFishingSpots(): List<FishingSpotBookmarkEntity>

    suspend fun checkFishingSpot(number: Int): Boolean

    suspend fun deleteFishingSpot(fishingSpotBookmarkEntity: FishingSpotBookmarkEntity)

    suspend fun deleteAllFishingSpots()
}
