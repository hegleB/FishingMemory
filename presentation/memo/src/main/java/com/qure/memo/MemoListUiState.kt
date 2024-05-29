package com.qure.memo

import com.qure.memo.model.MemoUI

sealed interface MemoListUiState {
    data object Loading : MemoListUiState
    data class Success(
        val memos: List<MemoUI>,
    ) : MemoListUiState
}