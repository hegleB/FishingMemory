package com.qure.domain.usecase.bookmark

import com.qure.domain.repository.FishingSpotLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.qure.domain.util.Result

class CheckFishingSpotBookmarkUseCase @Inject constructor(
    private val fishingSpotLocalRepository: FishingSpotLocalRepository,
) {
    suspend operator fun invoke(number: Int): Flow<Result<Boolean>> {
        return fishingSpotLocalRepository.checkFishingSpot(number)
    }
}