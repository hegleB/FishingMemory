package com.qure.domain.usecase.bookmark

import com.qure.domain.entity.fishingspot.FishingSpotBookmark
import com.qure.domain.repository.FishingSpotLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFishingSpotBookmarksUseCase
    @Inject
    constructor(
        private val fishingSpotLocalRepository: FishingSpotLocalRepository,
    ) {
        operator fun invoke(): Flow<List<FishingSpotBookmark>> {
            return fishingSpotLocalRepository.getFishingSpots()
        }
    }
