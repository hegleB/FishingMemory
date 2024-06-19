package com.qure.data.datasource.fishingspot

import com.qure.data.entity.fishingspot.FishingSpotBookmarkEntity
import com.qure.data.entity.fishingspot.FishingSpotLocalEntity
import com.qure.data.local.FishingBookmarkDao
import com.qure.data.local.FishingSpotDao
import javax.inject.Inject

internal class FishingSpotLocalDataSourceImpl
    @Inject
    constructor(
        private val fishingSpotBookmarkDao: FishingBookmarkDao,
        private val fishingSpotDao: FishingSpotDao,
    ) : FishingSpotLocalDataSource {
        override suspend fun insertFishingSpotBookmark(fishingSpotBookmarkEntity: FishingSpotBookmarkEntity) {
            fishingSpotBookmarkDao.insertFishingSpot(fishingSpotBookmarkEntity)
        }

        override suspend fun getFishingSpotBookmarks(): List<FishingSpotBookmarkEntity> {
            return fishingSpotBookmarkDao.getFishingSpots()
        }

        override suspend fun checkFishingSpotBookmark(number: Int): Boolean {
            return fishingSpotBookmarkDao.checkFishingSpot(number)
        }

        override suspend fun deleteFishingSpotBookmark(fishingSpotBookmarkEntity: FishingSpotBookmarkEntity) {
            fishingSpotBookmarkDao.deleteFishingSpot(fishingSpotBookmarkEntity)
        }

        override suspend fun deleteAllFishingSpotBookmarks() {
            fishingSpotBookmarkDao.deleteAllFishingSpots()
        }

    override suspend fun insertFishingSpots(fishingSpots: List<FishingSpotLocalEntity>) {
        fishingSpotDao.insertFishingSpots(fishingSpots)
    }

    override suspend fun getFishingSpots(): List<FishingSpotLocalEntity> {
        return fishingSpotDao.getFishingSpots()
    }
}
