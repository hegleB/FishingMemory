package com.qure.domain.usecase.bookmark

import com.qure.data.repository.fishingspot.FishingSpotLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckFishingSpotBookmarkUseCase
    @Inject
    constructor(
        private val fishingSpotLocalRepository: FishingSpotLocalRepository,
    ) {
        operator fun invoke(number: Int): Flow<Boolean> {
            return fishingSpotLocalRepository.checkFishingSpot(number)
        }
    }
