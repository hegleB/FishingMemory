package com.qure.create.model

import com.qure.core.extensions.Empty
import com.qure.domain.entity.map.Addresses
import com.qure.domain.entity.map.Region

data class GeocodingUI(
    val roadAddress: String = String.Empty,
    val jibunAddress: String = String.Empty,
    val englishAddress: String = String.Empty,
    val addressElements: Array<Region> = emptyArray(),
    val x: String = String.Empty,
    val y: String = String.Empty,
    val distance: Double = 0.0,
)

fun Addresses.toGeocodingUI(): GeocodingUI {
    return GeocodingUI(
        roadAddress = this.roadAddress,
        jibunAddress = this.jibunAddress,
        englishAddress = this.englishAddress,
        addressElements = this.addressElements,
        x = this.x,
        y = this.y,
        distance = this.distance,
    )
}