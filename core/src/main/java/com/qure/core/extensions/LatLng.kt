package com.qure.core.extensions

import com.naver.maps.geometry.LatLng
import ted.gun0912.clustering.geometry.TedLatLng


val DefaultLatLng
    get() = LatLng(String.DefaultLatitude.toDouble(), String.DefaultLongitude.toDouble())

fun LatLng.toCoordsString(): String {
    return "${this.latitude},${this.longitude}"
}

fun LatLng.toReverseCoordsString(): String {
    return "${this.longitude},${this.latitude}"
}

fun TedLatLng.toCoordsString(): String {
    return "${this.latitude},${this.longitude}"
}

fun TedLatLng.toReverseCoordsString(): String {
    return "${this.longitude},${this.latitude}"
}