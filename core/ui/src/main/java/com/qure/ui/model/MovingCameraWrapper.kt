package com.qure.ui.model

import android.location.Location

sealed interface MovingCameraWrapper {
    data object Default : MovingCameraWrapper
    data class MyLocation(val location: Location) : MovingCameraWrapper
    data class Moving(val location: Location) : MovingCameraWrapper
}

enum class MovingCameraType {
    MARKER,
    MY_LOCATION,
}