package com.qure.data.datasource.fishingspot

import com.qure.data.entity.fishingspot.FishingSpotBookmarkEntity

internal interface FishingSpotLocalDataSource {
    suspend fun insertFishingSpotBookmark(fishingSpotBookmarkEntity: FishingSpotBookmarkEntity)

    suspend fun getFishingSpotBookmarks(): List<FishingSpotBookmarkEntity>

    suspend fun checkFishingSpotBookmark(number: Int): Boolean

    suspend fun deleteFishingSpotBookmark(fishingSpotBookmarkEntity: FishingSpotBookmarkEntity)

    suspend fun deleteAllFishingSpotBookmarks()
}
