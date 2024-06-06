package com.qure.create

import com.qure.ui.model.MemoUI

sealed interface MemoCreateUiState {
    data object Loading : MemoCreateUiState
    data object Initial : MemoCreateUiState
    data class Success(
        val memo: MemoUI,
    ) : MemoCreateUiState
}