package com.qure.camera

import androidx.compose.runtime.Immutable
import com.qure.model.camera.ObjectRect

@Immutable
data class CameraUiState(
    val screenWidth: Int = 0,
    val screenHeight: Int = 0,
    val imageWidth: Int = 0,
    val imageHeight: Int = 0,
    val fishSize: ObjectRect<Int>? = null,
    val cardSize: ObjectRect<Int>? = null,
    val bodySize: Double? = null,
    val isLevelCorrect: Boolean = false,
    val isEnable: Boolean = false,
)
