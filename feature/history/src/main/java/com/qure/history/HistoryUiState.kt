package com.qure.history

sealed interface HistoryUiState {
    data object Empty : HistoryUiState
    data object Loading : HistoryUiState
    data class Success(
        val memos: List<com.qure.ui.model.MemoUI> = emptyList(),
    ) : HistoryUiState
}