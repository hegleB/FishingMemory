package com.qure.create.location

import com.qure.ui.model.ReverseGeocodingUI

sealed interface ReverseGeoCodingUiState {
    data object Idle : ReverseGeoCodingUiState
    data object Loading : ReverseGeoCodingUiState
    data class Success(
        val reverseGeocoding: ReverseGeocodingUI? = null,
    ) : ReverseGeoCodingUiState
}