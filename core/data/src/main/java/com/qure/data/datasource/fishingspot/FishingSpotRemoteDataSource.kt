package com.qure.data.datasource.fishingspot

import com.qure.data.entity.fishingspot.FishingSpotEntity

internal interface FishingSpotRemoteDataSource {
    suspend fun getFishingSpots(collectionId: String): List<FishingSpotEntity>
}
