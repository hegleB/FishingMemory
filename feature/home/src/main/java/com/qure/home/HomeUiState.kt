package com.qure.home

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.qure.ui.model.WeatherUI

@Stable
sealed interface HomeUiState {

    @Immutable
    data object Loading : HomeUiState

    @Immutable
    data class Success(
        val weather: List<WeatherUI> = emptyList(),
        val memos: List<com.qure.ui.model.MemoUI> = emptyList(),
    ) : HomeUiState
}