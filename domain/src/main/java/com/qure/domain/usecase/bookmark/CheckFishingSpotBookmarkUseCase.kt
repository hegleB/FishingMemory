package com.qure.domain.usecase.bookmark

import com.qure.domain.repository.FishingSpotLocalRepository
import com.qure.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckFishingSpotBookmarkUseCase
    @Inject
    constructor(
        private val fishingSpotLocalRepository: FishingSpotLocalRepository,
    ) {
        suspend operator fun invoke(number: Int): Flow<Result<Boolean>> {
            return fishingSpotLocalRepository.checkFishingSpot(number)
        }
    }
