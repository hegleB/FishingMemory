package com.qure.create.model

import com.qure.core.extensions.Empty
import com.qure.domain.entity.map.Geocoding
import com.qure.domain.entity.map.Region

data class GeocodingUI(
    val roadAddress: String = String.Empty,
    val jibunAddress: String = String.Empty,
    val englishAddress: String = String.Empty,
    val addressElements: Array<Region> = emptyArray(),
    val x: String = String.Empty,
    val y: String = String.Empty,
    val coords: String = String.Empty,
    val distance: Double = 0.0,
)

fun Geocoding.toGeocodingUI(): GeocodingUI {
    val addresses = this.addresses ?: emptyArray()
    if (addresses.isEmpty()) {
        return GeocodingUI()
    }
    val address = addresses[0]
    val coords = "${address.y},${address.x}"
    return GeocodingUI(
        roadAddress = address.roadAddress,
        jibunAddress = address.jibunAddress,
        englishAddress = address.englishAddress,
        addressElements = address.addressElements,
        x = address.x,
        y = address.y,
        coords = coords,
        distance = address.distance,
    )
}