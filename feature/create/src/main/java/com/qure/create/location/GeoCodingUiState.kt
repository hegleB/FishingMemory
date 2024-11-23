package com.qure.create.location

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.qure.ui.model.GeocodingUI

@Stable
sealed interface GeoCodingUiState {
    @Immutable
    data object Idle : GeoCodingUiState

    @Immutable
    data object Loading : GeoCodingUiState

    @Immutable
    data class Success(
        val geocoding: GeocodingUI? = null,
    ) : GeoCodingUiState
}