package com.qure.data.repository.fishingspot

import com.qure.data.datasource.fishingspot.FishingSpotLocalDataSource
import com.qure.data.datasource.fishingspot.FishingSpotRemoteDataSource
import com.qure.data.mapper.toFishingSpot
import com.qure.data.mapper.toFishingSpotLocalEntity
import com.qure.data.utils.NetworkMonitor
import com.qure.model.fishingspot.FishingSpot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class FishingSpotRepositoryImpl
@Inject
constructor(
    private val fishingSpotRemoteDataSource: FishingSpotRemoteDataSource,
    private val fishingSpotLocalDataSource: FishingSpotLocalDataSource,
    private val networkMonitor: NetworkMonitor,
) : FishingSpotRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFishingSpot(collectionId: String): Flow<List<FishingSpot>> {
        return networkMonitor.isConnectNetwork.flatMapLatest { isConnectNetwork ->
            if (isConnectNetwork) {
                fetchFishingSpotFromRemoteOrLocal(collectionId)
            } else {
                fetchFishingSpotsFromLocal()
            }
        }
    }

    private fun fetchFishingSpotFromRemoteOrLocal(collectionId: String): Flow<List<FishingSpot>> =
        flow {
            val localFishingSpot =
                fishingSpotLocalDataSource.getFishingSpots()
            if (localFishingSpot.isEmpty()) {
                val remoteFishingSpot = fishingSpotRemoteDataSource.getFishingSpots(collectionId)
                    .map { it.toFishingSpot() }
                emit(remoteFishingSpot)
                fishingSpotLocalDataSource.insertFishingSpots(remoteFishingSpot.map { it.toFishingSpotLocalEntity() })
            } else {
                emit(localFishingSpot.map { it.toFishingSpot() })
            }
        }

    private fun fetchFishingSpotsFromLocal(): Flow<List<FishingSpot>> =
        flow {
            val localFishingSpots = fishingSpotLocalDataSource.getFishingSpots()
            emit(localFishingSpots.map { it.toFishingSpot() })
        }
}
