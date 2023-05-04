package com.qure.data.datasource.fishingspot

import com.qure.data.entity.fishingspot.FishingSpotBookmarkEntity
import com.qure.data.local.FishingSpotDao
import javax.inject.Inject

class FishingSpotLocalDataSourceImpl @Inject constructor(
    private val fishingSpotDao: FishingSpotDao
) : FishingSpotLocalDataSource {

    override suspend fun insertFishingSpot(fishingSpotBookmarkEntity: FishingSpotBookmarkEntity) {
        fishingSpotDao.insertFishingSpot(fishingSpotBookmarkEntity)
    }

    override fun getFishingSpots(): List<FishingSpotBookmarkEntity> {
        return fishingSpotDao.getFishingSpots()
    }

    override suspend fun checkFishingSpot(number: Int): Boolean {
        return fishingSpotDao.checkFishingSpot(number)
    }

    override suspend fun deleteFishingSpot(fishingSpotBookmarkEntity: FishingSpotBookmarkEntity) {
        fishingSpotDao.deleteFishingSpot(fishingSpotBookmarkEntity)
    }

    override suspend fun deleteAllFishingSpots() {
        fishingSpotDao.deleteAllFishingSpots()
    }
}