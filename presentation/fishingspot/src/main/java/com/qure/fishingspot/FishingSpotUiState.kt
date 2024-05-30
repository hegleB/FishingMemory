package com.qure.fishingspot

import com.qure.model.FishingSpotUI

sealed interface FishingSpotUiState {
    data object Loading : FishingSpotUiState
    data class Success(
        val bookmarks: List<FishingSpotUI>,
    ) : FishingSpotUiState
}