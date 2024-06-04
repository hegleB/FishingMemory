package com.qure.data.repository.fishingspot

import com.qure.model.fishingspot.FishingSpot
import com.qure.model.fishingspot.FishingSpotQuery
import kotlinx.coroutines.flow.Flow

interface FishingSpotRepository {
    fun getFishingSpot(fishingSpotQuery: FishingSpotQuery): Flow<List<FishingSpot>>
}
