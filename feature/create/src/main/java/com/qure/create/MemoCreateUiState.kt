package com.qure.create

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.qure.ui.model.MemoUI

@Stable
sealed interface MemoCreateUiState {
    @Immutable
    data object Loading : MemoCreateUiState

    @Immutable
    data object Initial : MemoCreateUiState

    @Immutable
    data class Success(
        val memo: MemoUI,
    ) : MemoCreateUiState
}