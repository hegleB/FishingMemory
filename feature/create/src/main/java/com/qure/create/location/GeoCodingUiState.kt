package com.qure.create.location

import com.qure.ui.model.GeocodingUI
import com.qure.ui.model.ReverseGeocodingUI

sealed interface GeoCodingUiState {
    data object Loading : GeoCodingUiState
    data class Success(
        val geocoding: GeocodingUI? = null,
        val reverseGeocoding: ReverseGeocodingUI? = null,
    ) : GeoCodingUiState
}