package com.qure.camera

import com.qure.model.camera.ObjectRect
import com.qure.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.round
import kotlin.math.roundToInt

@HiltViewModel
class CameraViewModel @Inject constructor() : BaseViewModel() {

    private val _cameraUiState = MutableStateFlow(CameraUiState())
    val cameraUiState = _cameraUiState.asStateFlow()

    fun setDetectedRect(
        rect: List<ObjectRect<Int>>
    ) {
        when {
            rect.size >= 2 -> {
                if (getHeightCenter(rect[0]) >= getHeightCenter(rect[1])) {
                    _cameraUiState.update { it.copy(fishSize = rect[1], cardSize = rect[0]) }
                } else {
                    _cameraUiState.update { it.copy(fishSize = rect[0], cardSize = rect[1]) }
                }
            }

            rect.size == 1 -> {
                _cameraUiState.update { it.copy(fishSize = rect[0], cardSize = null) }
            }

            else -> {
                _cameraUiState.update { it.copy(fishSize = null, cardSize = null) }
            }
        }
        calculateSize()
    }


    private fun calculateSize() {
        val fishRect = _cameraUiState.value.fishSize
        val cardRect = _cameraUiState.value.cardSize

        if (fishRect != null && cardRect != null) {
            val fishSize = fishRect.left - fishRect.right
            val cardSize = cardRect.left - cardRect.right
            _cameraUiState.update { it.copy(bodySize = round(fishSize.toDouble() / cardSize * 8.56f * 100) / 100) }
        } else {
            _cameraUiState.update { it.copy(bodySize = null) }
        }
    }


    private fun getHeightCenter(rect: ObjectRect<Int>): Int {
        return (rect.top + rect.bottom) / 2
    }

    fun changeLevel(x: Float, y: Float) {
        _cameraUiState.update { it.copy(isLevelCorrect = x.roundToInt() <= 10 && y.roundToInt() <= 10) }
    }
}