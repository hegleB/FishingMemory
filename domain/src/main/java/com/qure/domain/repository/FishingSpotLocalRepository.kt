package com.qure.domain.repository

import com.qure.domain.entity.fishingspot.FishingSpotBookmark
import com.qure.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface FishingSpotLocalRepository {
    suspend fun insertFishingSpot(fishingSpotBookmark: FishingSpotBookmark)

    suspend fun getFishingSpots(): Flow<Result<List<FishingSpotBookmark>>>

    suspend fun checkFishingSpot(number: Int): Flow<Result<Boolean>>

    suspend fun deleteFishingSpot(fishingSpotBookmark: FishingSpotBookmark)

    suspend fun deleteAllFishingSpots()
}
