package com.qure.domain.usecase.bookmark

import com.qure.data.repository.fishingspot.FishingSpotLocalRepository
import javax.inject.Inject

class DeleteAllFishingSpotBookmarkUseCase
    @Inject
    constructor(
        private val fishingSpotLocalRepository: FishingSpotLocalRepository,
    ) {
        suspend operator fun invoke() {
            fishingSpotLocalRepository.deleteAllFishingSpots()
        }
    }
