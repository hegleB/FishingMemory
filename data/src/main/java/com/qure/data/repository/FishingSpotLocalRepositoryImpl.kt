package com.qure.data.repository

import com.qure.data.datasource.fishingspot.FishingSpotLocalDataSource
import com.qure.data.mapper.toFishingSpotBookmark
import com.qure.data.mapper.toFishingSpotBookmarkEntity
import com.qure.domain.entity.fishingspot.FishingSpotBookmark
import com.qure.domain.repository.FishingSpotLocalRepository
import com.qure.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FishingSpotLocalRepositoryImpl
    @Inject
    constructor(
        private val fishingSpotLocalDataSource: FishingSpotLocalDataSource,
    ) : FishingSpotLocalRepository {
        override suspend fun insertFishingSpot(fishingSpotBookmark: FishingSpotBookmark) {
            fishingSpotLocalDataSource.insertFishingSpot(fishingSpotBookmark.toFishingSpotBookmarkEntity())
        }

        override suspend fun getFishingSpots(): Flow<Result<List<FishingSpotBookmark>>> {
            return flow {
                emit(Result.Loading)
                val fishingSpots =
                    fishingSpotLocalDataSource.getFishingSpots().map { it.toFishingSpotBookmark() }
                if (fishingSpots.isNotEmpty()) {
                    emit(Result.Success(fishingSpots))
                } else {
                    emit(Result.Empty)
                }
            }.catch { throwable ->
                emit(Result.Error(throwable))
            }
        }

        override suspend fun checkFishingSpot(number: Int): Flow<Result<Boolean>> {
            return flow {
                emit(Result.Loading)
                val existFishingSpot = fishingSpotLocalDataSource.checkFishingSpot(number)
                if (existFishingSpot) {
                    emit(Result.Success(true))
                } else {
                    emit(Result.Empty)
                }
            }.catch { throwalbe ->
                emit(Result.Error(throwalbe))
            }
        }

        override suspend fun deleteFishingSpot(fishingSpotBookmark: FishingSpotBookmark) {
            fishingSpotLocalDataSource.deleteFishingSpot(fishingSpotBookmark.toFishingSpotBookmarkEntity())
        }

        override suspend fun deleteAllFishingSpots() {
            fishingSpotLocalDataSource.deleteAllFishingSpots()
        }
    }
