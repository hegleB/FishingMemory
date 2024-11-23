package com.qure.map

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.qure.ui.model.FishingPlaceInfo

@Stable
sealed interface MapUiState {
    @Immutable
    data object Loading : MapUiState

    @Immutable
    data class Success(
        val fishingSpots: List<FishingPlaceInfo.FishingSpotInfo> = emptyList(),
        val memos: List<FishingPlaceInfo.MemoInfo> = emptyList(),
    ) : MapUiState
}