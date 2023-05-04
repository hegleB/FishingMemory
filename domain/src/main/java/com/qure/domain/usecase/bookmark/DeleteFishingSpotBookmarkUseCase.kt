package com.qure.domain.usecase.bookmark

import com.qure.domain.entity.fishingspot.FishingSpotBookmark
import com.qure.domain.repository.FishingSpotLocalRepository
import javax.inject.Inject

class DeleteFishingSpotBookmarkUseCase @Inject constructor(
    private val fishingSpotLocalRepository: FishingSpotLocalRepository,
) {
    suspend operator fun invoke(fishingSpotBookmark: FishingSpotBookmark) {
        fishingSpotLocalRepository.deleteFishingSpot(fishingSpotBookmark)
    }
}