package com.qure.data.repository.fishingspot

import com.qure.data.datasource.fishingspot.FishingSpotRemoteDataSource
import com.qure.data.mapper.toFishingSpot
import com.qure.model.fishingspot.FishingSpot
import com.qure.model.fishingspot.FishingSpotQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class FishingSpotRepositoryImpl
@Inject
constructor(
    private val fishingSpotRemoteDataSource: FishingSpotRemoteDataSource,
) : FishingSpotRepository {
    override fun getFishingSpot(fishingSpotQuery: FishingSpotQuery): Flow<List<FishingSpot>> {
        return flow {
            emit(fishingSpotRemoteDataSource.getFishingSopt(fishingSpotQuery).map { it.toFishingSpot() })
        }
    }
}
