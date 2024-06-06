package com.qure.splash

import com.qure.model.user.SplashState

sealed interface SplashUiState {
    data object Loading: SplashUiState
    data class Success(val onboardingState: SplashState): SplashUiState
}