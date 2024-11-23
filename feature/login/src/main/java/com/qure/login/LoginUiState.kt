package com.qure.login

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable


@Stable
sealed interface LoginUiState {
    @Immutable
    data object Loading : LoginUiState

    @Immutable
    data object Initial : LoginUiState

    @Immutable
    data object LaunchLogin: LoginUiState

    @Immutable
    data object Success : LoginUiState
}