package com.qure.home

import com.qure.ui.model.WeatherUI

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val weather: List<WeatherUI> = emptyList(),
        val memos: List<com.qure.ui.model.MemoUI> = emptyList(),
    ) : HomeUiState
}