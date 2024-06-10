package com.qure.data.repository.fishingspot

import com.qure.data.datasource.fishingspot.FishingSpotLocalDataSource
import com.qure.data.mapper.toFishingSpotBookmark
import com.qure.data.mapper.toFishingSpotBookmarkEntity
import com.qure.model.fishingspot.FishingSpotBookmark
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class FishingSpotLocalRepositoryImpl
@Inject
constructor(
    private val fishingSpotLocalDataSource: FishingSpotLocalDataSource,
) : FishingSpotLocalRepository {
    override suspend fun insertFishingSpot(fishingSpotBookmark: FishingSpotBookmark) {
        fishingSpotLocalDataSource.insertFishingSpot(fishingSpotBookmark.toFishingSpotBookmarkEntity())
    }

    override fun getFishingSpots(): Flow<List<FishingSpotBookmark>> {
        return flow {
            emit(fishingSpotLocalDataSource.getFishingSpots().map { it.toFishingSpotBookmark() })
        }
    }

    override fun checkFishingSpot(number: Int): Flow<Boolean> {
        return flow {
            emit(fishingSpotLocalDataSource.checkFishingSpot(number))
        }
    }

    override suspend fun deleteFishingSpot(fishingSpotBookmark: FishingSpotBookmark) {
        fishingSpotLocalDataSource.deleteFishingSpot(fishingSpotBookmark.toFishingSpotBookmarkEntity())
    }

    override suspend fun deleteAllFishingSpots() {
        fishingSpotLocalDataSource.deleteAllFishingSpots()
    }
}
