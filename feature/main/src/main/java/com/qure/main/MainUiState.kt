package com.qure.main

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.qure.model.user.UserData

@Stable
sealed interface MainUiState {
    @Immutable
    data object Loading : MainUiState

    @Immutable
    data class Success(val userData: UserData) : MainUiState
}