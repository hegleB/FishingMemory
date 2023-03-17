package com.qure.splash

import com.qure.core.BaseViewModel
import com.qure.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): BaseViewModel() {

    fun isSignedUp(): Boolean {
        return authRepository.getAccessTokenFromLocal().isNotEmpty()
    }
}