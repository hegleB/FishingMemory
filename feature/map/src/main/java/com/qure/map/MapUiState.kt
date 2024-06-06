package com.qure.map

import com.qure.ui.model.FishingPlaceInfo

sealed interface MapUiState {
    data object Loading : MapUiState
    data class Success(
        val fishingSpots: List<FishingPlaceInfo.FishingSpotInfo> = emptyList(),
        val memos: List<FishingPlaceInfo.MemoInfo> = emptyList(),
    ) : MapUiState
}