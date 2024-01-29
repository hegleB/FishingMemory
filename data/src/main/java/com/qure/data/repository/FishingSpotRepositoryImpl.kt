package com.qure.data.repository

import com.qure.data.datasource.fishingspot.FishingSpotRemoteDataSource
import com.qure.data.mapper.toFishingSpot
import com.qure.domain.entity.fishingspot.FishingSpot
import com.qure.domain.entity.fishingspot.FishingSpotQuery
import com.qure.domain.repository.FishingSpotRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FishingSpotRepositoryImpl
    @Inject
    constructor(
        private val fishingSpotRemoteDataSource: FishingSpotRemoteDataSource,
    ) : FishingSpotRepository {
        override fun getFishingSpot(fishingSpotQuery: FishingSpotQuery): Flow<Result<List<FishingSpot>>> {
            return flow {
                fishingSpotRemoteDataSource.getFishingSopt(fishingSpotQuery)
                    .onSuccess {
                        emit(Result.success(it.map { it.toFishingSpot() }))
                    }.onFailure { throwable ->
                        emit(Result.failure(throwable))
                    }
            }
        }
    }
