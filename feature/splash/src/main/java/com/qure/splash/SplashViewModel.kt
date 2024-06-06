package com.qure.splash

import androidx.lifecycle.viewModelScope
import com.qure.data.repository.auth.AuthRepository
import com.qure.data.repository.user.UserDataRepository
import com.qure.domain.usecase.onboarding.ReadOnboardingUseCase
import com.qure.model.onboarding.OnboardingType
import com.qure.model.user.SplashState
import com.qure.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject
constructor(
    private val authRepository: AuthRepository,
    private val readOnboardingUseCase: ReadOnboardingUseCase,
    private val userDataRepository: UserDataRepository,
) : BaseViewModel() {

    private val kakaoDeepLinkState: Flow<Boolean> =
        userDataRepository.userData.map { it.isKakaoDeepLink }
    val splashUiState: StateFlow<SplashUiState> =
        splashUiState(userDataRepository)
            .map { SplashUiState.Success(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SplashUiState.Loading,
            )

    init {
        setOnboardingState()
    }

    private fun splashUiState(
        userDataRepository: UserDataRepository,
    ): StateFlow<SplashState> =
        combine(
            kakaoDeepLinkState,
            userDataRepository.userData,
        ) { isKakaoDeepLink, userData ->
            if (isKakaoDeepLink && userData.splashState == SplashState.LOGGED_IN) {
                SplashState.DEEPLINK
            } else {
                userData.splashState
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SplashState.OPEN,
        )


    private fun setOnboardingState() {
        viewModelScope.launch {
            val onboardingState = when (readOnboardingUseCase(OnboardingType.AFTER_SPLASH)) {
                null -> SplashState.OPEN
                else -> when (authRepository.getAccessTokenFromLocal().isNotEmpty()) {
                    true -> SplashState.LOGGED_IN
                    else -> SplashState.NOT_LOGGED_IN
                }
            }
            userDataRepository.setSplashState(onboardingState)
        }
    }
}
