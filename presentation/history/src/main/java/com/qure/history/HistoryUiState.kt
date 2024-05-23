package com.qure.history

import com.qure.memo.model.MemoUI

sealed interface HistoryUiState {
    data object Empty : HistoryUiState
    data object Loading : HistoryUiState
    data class Success(
        val memos: List<MemoUI> = emptyList(),
    ) : HistoryUiState
}