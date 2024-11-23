package com.qure.memo

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.qure.ui.model.MemoUI

@Stable
sealed interface MemoListUiState {
    @Immutable
    data object Loading : MemoListUiState

    @Immutable
    data class Success(
        val memos: List<MemoUI>,
    ) : MemoListUiState
}