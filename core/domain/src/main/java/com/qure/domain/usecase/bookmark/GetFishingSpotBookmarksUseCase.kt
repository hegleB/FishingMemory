package com.qure.domain.usecase.bookmark

import com.qure.data.repository.fishingspot.FishingSpotLocalRepository
import com.qure.model.fishingspot.FishingSpotBookmark
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
