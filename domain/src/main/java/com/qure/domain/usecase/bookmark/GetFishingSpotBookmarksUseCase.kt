package com.qure.domain.usecase.bookmark

import com.qure.domain.entity.fishingspot.FishingSpotBookmark
import com.qure.domain.repository.FishingSpotLocalRepository
import com.qure.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFishingSpotBookmarksUseCase
    @Inject
    constructor(
        private val fishingSpotLocalRepository: FishingSpotLocalRepository,
    ) {
        suspend operator fun invoke(): Flow<Result<List<FishingSpotBookmark>>> {
            return fishingSpotLocalRepository.getFishingSpots()
        }
    }
