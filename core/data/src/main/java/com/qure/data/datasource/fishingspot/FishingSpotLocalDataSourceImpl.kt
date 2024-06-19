package com.qure.data.datasource.fishingspot

import com.qure.data.entity.fishingspot.FishingSpotBookmarkEntity
import com.qure.data.local.FishingBookmarkDao
import javax.inject.Inject

internal class FishingSpotLocalDataSourceImpl
    @Inject
    constructor(
        private val fishingSpotDao: FishingBookmarkDao,
    ) : FishingSpotLocalDataSource {
        override suspend fun insertFishingSpotBookmark(fishingSpotBookmarkEntity: FishingSpotBookmarkEntity) {
            fishingSpotDao.insertFishingSpot(fishingSpotBookmarkEntity)
        }

        override suspend fun getFishingSpotBookmarks(): List<FishingSpotBookmarkEntity> {
            return fishingSpotDao.getFishingSpots()
        }

        override suspend fun checkFishingSpotBookmark(number: Int): Boolean {
            return fishingSpotDao.checkFishingSpot(number)
        }

        override suspend fun deleteFishingSpotBookmark(fishingSpotBookmarkEntity: FishingSpotBookmarkEntity) {
            fishingSpotDao.deleteFishingSpot(fishingSpotBookmarkEntity)
        }

        override suspend fun deleteAllFishingSpotBookmarks() {
            fishingSpotDao.deleteAllFishingSpots()
        }
    }
