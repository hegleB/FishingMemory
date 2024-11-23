package com.qure.create.location

import androidx.compose.runtime.Immutable
import com.qure.ui.model.GeocodingUI
import com.qure.ui.model.ReverseGeocodingUI

@Immutable
data class LocationUiState(
    val geocoding: GeocodingUI? = null,
    val reverseGeocoding: ReverseGeocodingUI? = null,
    val region: Regions = Regions.REGION,
    val currentPage: Int = 0,
    val regions: List<String> = emptyList(),
    val selectedRegions: List<String> = List(3) { "" },
)