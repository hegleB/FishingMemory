package com.qure.history

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface HistoryUiState {
    @Immutable
    data object Loading : HistoryUiState

    @Immutable
    data class Success(
        val memos: List<com.qure.ui.model.MemoUI> = emptyList(),
    ) : HistoryUiState
}