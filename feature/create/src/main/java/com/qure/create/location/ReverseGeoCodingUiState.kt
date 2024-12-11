package com.qure.create.location

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.qure.ui.model.ReverseGeocodingUI

@Stable
sealed interface ReverseGeoCodingUiState {
    @Immutable
    data object Idle : ReverseGeoCodingUiState
    @Immutable
    data object Loading : ReverseGeoCodingUiState
    @Immutable
    data class Success(
        val reverseGeocoding: ReverseGeocodingUI? = null,
    ) : ReverseGeoCodingUiState
}