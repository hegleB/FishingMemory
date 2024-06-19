package com.qure.data.repository.fishingspot

import com.qure.model.fishingspot.FishingSpot
import kotlinx.coroutines.flow.Flow

interface FishingSpotRepository {
    fun getFishingSpot(collectionId: String): Flow<List<FishingSpot>>
}
