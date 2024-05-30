package com.qure.core.extensions

import com.naver.maps.geometry.LatLng
import ted.gun0912.clustering.geometry.TedLatLng

fun TedLatLng.toLatlng(): LatLng {
    return LatLng(this.latitude, this.longitude)
}

fun TedLatLng.toReverseLatlng(): LatLng {
    return LatLng(this.longitude, this.latitude)
}

fun TedLatLng.toReverseCoordsString(): String {
    return "${this.latitude},${this.longitude}"
}
