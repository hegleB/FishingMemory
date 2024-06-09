package com.qure.create.location

import com.qure.ui.model.GeocodingUI

sealed interface GeoCodingUiState {
    data object Idle : GeoCodingUiState
    data object Loading : GeoCodingUiState
    data class Success(
        val geocoding: GeocodingUI? = null,
    ) : GeoCodingUiState
}