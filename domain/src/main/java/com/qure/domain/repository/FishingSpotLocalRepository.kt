package com.qure.domain.repository

import com.qure.domain.entity.fishingspot.FishingSpotBookmark
import kotlinx.coroutines.flow.Flow

interface FishingSpotLocalRepository {
    suspend fun insertFishingSpot(fishingSpotBookmark: FishingSpotBookmark)

    fun getFishingSpots(): Flow<List<FishingSpotBookmark>>

    fun checkFishingSpot(number: Int): Flow<Boolean>

    suspend fun deleteFishingSpot(fishingSpotBookmark: FishingSpotBookmark)

    suspend fun deleteAllFishingSpots()
}
