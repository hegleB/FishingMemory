package com.qure.mypage

import androidx.lifecycle.viewModelScope
import com.qure.data.repository.auth.AuthRepository
import com.qure.domain.usecase.member.LogoutUserUseCase
import com.qure.domain.usecase.member.WithdrawServiceUseCase
import com.qure.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel
    @Inject
    constructor(
        private val logoutUserUseCase: LogoutUserUseCase,
        private val authRepository: AuthRepository,
        private val withdrawServiceUseCase: WithdrawServiceUseCase,
    ) : BaseViewModel() {
        private val _logoutSucceed: MutableStateFlow<Boolean> = MutableStateFlow(false)
        val logoutSucceed = _logoutSucceed.asStateFlow()

        private val _withdrawSucceed: MutableStateFlow<Boolean> = MutableStateFlow(false)
        val withdrawSucceed = _withdrawSucceed.asStateFlow()

        val email = authRepository.getEmailFromLocal()
        fun logoutUser() {
            viewModelScope.launch {
                logoutUserUseCase(authRepository.getEmailFromLocal())
                    .catch { throwable -> sendErrorMessage(throwable) }
                    .collect {
                        _logoutSucceed.emit(true)
                    }
            }
        }

        fun withdrawService() {
            viewModelScope.launch {
                withdrawServiceUseCase()
                    .catch { throwable -> sendErrorMessage(throwable) }
                    .collect {
                        _withdrawSucceed.emit(true)
                    }
            }
        }
    }
