package com.qure.domain.usecase.fishingspot

import com.qure.domain.entity.fishingspot.FishingSpot
import com.qure.domain.entity.fishingspot.FishingSpotQuery
import com.qure.domain.repository.FishingSpotRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFishingSpotUseCase
    @Inject
    constructor(
        private val fishingSpotRepository: FishingSpotRepository,
    ) {
        operator fun invoke(fishingSpotQuery: FishingSpotQuery): Flow<List<FishingSpot>> {
            return fishingSpotRepository.getFishingSpot(fishingSpotQuery)
        }
    }
