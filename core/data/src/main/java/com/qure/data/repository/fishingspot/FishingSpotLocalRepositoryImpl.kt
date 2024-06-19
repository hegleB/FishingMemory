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
    override suspend fun insertFishingSpotBookmark(fishingSpotBookmark: FishingSpotBookmark) {
        fishingSpotLocalDataSource.insertFishingSpotBookmark(fishingSpotBookmark.toFishingSpotBookmarkEntity())
    }

    override fun getFishingSpotBookmarks(): Flow<List<FishingSpotBookmark>> {
        return flow {
            emit(fishingSpotLocalDataSource.getFishingSpotBookmarks().map { it.toFishingSpotBookmark() })
        }
    }

    override fun checkFishingSpotBookmark(number: Int): Flow<Boolean> {
        return flow {
            emit(fishingSpotLocalDataSource.checkFishingSpotBookmark(number))
        }
    }

    override suspend fun deleteFishingSpotBookmark(fishingSpotBookmark: FishingSpotBookmark) {
        fishingSpotLocalDataSource.deleteFishingSpotBookmark(fishingSpotBookmark.toFishingSpotBookmarkEntity())
    }

    override suspend fun deleteAllFishingSpotBookmarks() {
        fishingSpotLocalDataSource.deleteAllFishingSpotBookmarks()
    }
}
