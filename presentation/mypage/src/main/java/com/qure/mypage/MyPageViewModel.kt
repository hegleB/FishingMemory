package com.qure.mypage

import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.domain.repository.AuthRepository
import com.qure.domain.usecase.member.LogoutUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val logoutUserUseCase: LogoutUserUseCase,
    private val authRepository: AuthRepository,
) : BaseViewModel() {

    private val _logoutSucceed: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val logutSucceed: MutableStateFlow<Boolean>
        get() = _logoutSucceed

    fun logoutUser() {
        viewModelScope.launch {
            logoutUserUseCase(authRepository.getEmailFromLocal()).collect { response ->
                response.onSuccess {
                    _logoutSucceed.emit(true)
                }.onFailure { thorwable ->
                    sendErrorMessage(thorwable)
                }
            }
        }
    }
}