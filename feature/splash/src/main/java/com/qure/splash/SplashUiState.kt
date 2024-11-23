package com.qure.splash

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.qure.model.user.SplashState

@Stable
sealed interface SplashUiState {
    @Immutable
    data object Loading: SplashUiState

    @Immutable
    data class Success(val onboardingState: SplashState): SplashUiState
}