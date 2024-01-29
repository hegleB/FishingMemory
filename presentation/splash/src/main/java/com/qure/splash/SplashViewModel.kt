package com.qure.splash

import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.domain.entity.OnboardingType
import com.qure.domain.repository.AuthRepository
import com.qure.domain.usecase.onboarding.ReadOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val readOnboardingUseCase: ReadOnboardingUseCase,
    ) : BaseViewModel() {
        private val _isFirstVisitor = MutableStateFlow(false)
        val isFirstVisitor: StateFlow<Boolean>
            get() = _isFirstVisitor

        init {
            checkFirstVisitor()
        }

        fun isSignedUp(): Boolean {
            return authRepository.getAccessTokenFromLocal().isNotEmpty()
        }

        fun checkFirstVisitor() =
            viewModelScope.launch {
                val response = readOnboardingUseCase(OnboardingType.AFTER_SPLASH)
                _isFirstVisitor.update {
                    response == null
                }
            }
    }
