package com.qure.memo.detail

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface DetailMemoUiState {
    @Immutable
    data object Loading : DetailMemoUiState

    @Immutable
    data object Initial : DetailMemoUiState

    @Immutable
    data object Success : DetailMemoUiState
}