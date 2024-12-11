package com.qure.fishingspot

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.qure.model.FishingSpotUI
import kotlinx.collections.immutable.ImmutableList

@Stable
sealed interface FishingSpotUiState {
    @Immutable
    data object Loading : FishingSpotUiState

    @Immutable
    data class Success(
        val bookmarks: ImmutableList<FishingSpotUI>,
    ) : FishingSpotUiState
}