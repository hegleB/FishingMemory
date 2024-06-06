package com.qure.main

import com.qure.model.user.UserData


sealed interface MainUiState {
    data object Loading : MainUiState
    data class Success(val userData: UserData) : MainUiState
}