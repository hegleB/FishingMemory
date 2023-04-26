package com.qure.core.extensions

import com.naver.maps.geometry.LatLng
import ted.gun0912.clustering.geometry.TedLatLng

fun LatLng.toReverseCoordsString(): String {
    return "${this.longitude},${this.latitude}"
}

fun TedLatLng.toReverseCoordsString(): String {
    return "${this.longitude},${this.latitude}"
}