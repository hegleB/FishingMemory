package com.qure.create.location

import com.qure.create.model.GeocodingUI
import com.qure.create.model.ReverseGeocodingUI

data class LocationUiState(
    val geocoding: GeocodingUI? = null,
    val reverseGeocoding: ReverseGeocodingUI? = null,
    val currentPage: Int = 0,
    val doIndex: Int = -1,
    val cityIndex: Int = -1,
    val regions: List<String> = emptyList(),
    val selectedRegions: List<String> = List(3) { "" },
)