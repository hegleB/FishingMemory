package com.qure.ui.extentions

import com.naver.maps.geometry.LatLng

fun LatLng.toReverseCoordsString(): String {
    return "${this.longitude},${this.latitude}"
}

fun LatLng.toCoordsString(): String {
    return "${this.latitude},${this.longitude}"
}