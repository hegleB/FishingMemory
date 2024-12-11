package com.qure.create.location

import androidx.compose.runtime.Immutable
import com.qure.ui.model.GeocodingUI
import com.qure.ui.model.ReverseGeocodingUI
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class LocationUiState(
    val geocoding: GeocodingUI? = null,
    val reverseGeocoding: ReverseGeocodingUI? = null,
    val region: Regions = Regions.REGION,
    val currentPage: Int = 0,
    val regions: ImmutableList<String> = persistentListOf(),
    val selectedRegions: List<String> = List(3) { "" },
)