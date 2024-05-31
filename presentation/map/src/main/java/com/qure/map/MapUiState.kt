package com.qure.map

sealed interface MapUiState {
    data object Loading : MapUiState
    data class Success(
        val fishingSpots: List<FishingPlaceInfo.FishingSpotInfo> = emptyList(),
        val memos: List<FishingPlaceInfo.MemoInfo> = emptyList(),
    ) : MapUiState
}