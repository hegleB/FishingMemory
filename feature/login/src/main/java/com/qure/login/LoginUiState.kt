package com.qure.login

sealed interface LoginUiState {
    data object Loading : LoginUiState
    data object Initial : LoginUiState
    data object LaunchLogin: LoginUiState
    data object Success : LoginUiState
}