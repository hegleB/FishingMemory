package com.qure.memo.detail

sealed interface DetailMemoUiState {
    data object Loading : DetailMemoUiState
    data object Initial : DetailMemoUiState
    data object Success : DetailMemoUiState
}