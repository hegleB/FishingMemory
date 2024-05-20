package com.qure.domain.repository

import com.qure.domain.entity.fishingspot.FishingSpot
import com.qure.domain.entity.fishingspot.FishingSpotQuery
import kotlinx.coroutines.flow.Flow

interface FishingSpotRepository {
    fun getFishingSpot(fishingSpotQuery: FishingSpotQuery): Flow<List<FishingSpot>>
}
