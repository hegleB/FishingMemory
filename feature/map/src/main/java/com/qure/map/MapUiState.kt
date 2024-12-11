package com.qure.map

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.qure.ui.model.FishingPlaceInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
sealed interface MapUiState {
    @Immutable
    data object Loading : MapUiState

    @Immutable
    data class Success(
        val fishingSpots: ImmutableList<FishingPlaceInfo.FishingSpotInfo> = persistentListOf(),
        val memos: ImmutableList<FishingPlaceInfo.MemoInfo> = persistentListOf(),
    ) : MapUiState
}