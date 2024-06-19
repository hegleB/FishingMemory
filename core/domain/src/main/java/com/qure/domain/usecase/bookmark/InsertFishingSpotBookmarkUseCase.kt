package com.qure.domain.usecase.bookmark

import com.qure.data.repository.fishingspot.FishingSpotLocalRepository
import com.qure.model.fishingspot.FishingSpotBookmark
import javax.inject.Inject

class InsertFishingSpotBookmarkUseCase
    @Inject
    constructor(
        private val fishingSpotLocalRepository: FishingSpotLocalRepository,
    ) {
        suspend operator fun invoke(fishingSpotBookmark: FishingSpotBookmark) {
            fishingSpotLocalRepository.insertFishingSpotBookmark(fishingSpotBookmark)
        }
    }
