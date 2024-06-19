package com.qure.data.repository.fishingspot

import com.qure.model.fishingspot.FishingSpotBookmark
import kotlinx.coroutines.flow.Flow

interface FishingSpotLocalRepository {
    suspend fun insertFishingSpotBookmark(fishingSpotBookmark: FishingSpotBookmark)

    fun getFishingSpotBookmarks(): Flow<List<FishingSpotBookmark>>

    fun checkFishingSpotBookmark(number: Int): Flow<Boolean>

    suspend fun deleteFishingSpotBookmark(fishingSpotBookmark: FishingSpotBookmark)

    suspend fun deleteAllFishingSpotBookmarks()
}
