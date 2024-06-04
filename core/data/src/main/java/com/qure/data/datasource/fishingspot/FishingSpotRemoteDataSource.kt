package com.qure.data.datasource.fishingspot

import com.qure.data.entity.fishingspot.FishingSpotEntity
import com.qure.model.fishingspot.FishingSpotQuery

interface FishingSpotRemoteDataSource {
    suspend fun getFishingSopt(fishingSpotQuery: FishingSpotQuery): List<FishingSpotEntity>
}