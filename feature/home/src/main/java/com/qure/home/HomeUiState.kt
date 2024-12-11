package com.qure.home

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.qure.ui.model.WeatherUI
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.qure.ui.model.MemoUI

@Stable
sealed interface HomeUiState {

    @Immutable
    data object Loading : HomeUiState

    @Immutable
    data class Success(
        val weather: ImmutableList<WeatherUI> = persistentListOf(),
        val memos: ImmutableList<MemoUI> = persistentListOf(),
    ) : HomeUiState
}