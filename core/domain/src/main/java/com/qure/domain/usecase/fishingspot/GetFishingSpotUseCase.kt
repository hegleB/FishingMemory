package com.qure.domain.usecase.fishingspot

import com.qure.data.repository.fishingspot.FishingSpotRepository
import com.qure.model.fishingspot.FishingSpot
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFishingSpotUseCase @Inject constructor(
    private val fishingSpotRepository: FishingSpotRepository,
) {
    operator fun invoke(collectionId: String): Flow<List<FishingSpot>> {
        return fishingSpotRepository.getFishingSpot(collectionId)

    }
}
