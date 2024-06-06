package com.qure.login

import androidx.lifecycle.viewModelScope
import com.qure.data.repository.auth.AuthRepository
import com.qure.data.repository.user.UserDataRepository
import com.qure.domain.usecase.auth.CreateUserUseCase
import com.qure.domain.usecase.auth.GetUserTokenUseCase
import com.qure.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val getUserTokenUseCase: GetUserTokenUseCase,
    private val authRepository: AuthRepository,
    private val userDataRepository: UserDataRepository,
    ) : BaseViewModel() {

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val loginUiState = _loginUiState.asStateFlow()

    fun createUser(
        email: String,
        accessToken: String,
    ) = viewModelScope.launch {
        createUserUseCase(email, accessToken)
            .onStart { _loginUiState.value = LoginUiState.Loading }
            .catch { throwable ->
                if (isExistsEmail(throwable.message)) {
                    handleExistingUser(email, accessToken)
                } else {
                    sendErrorMessage(throwable)
                }
            }.collectLatest {
                authRepository.saveEmailToLocal(email)
                authRepository.saveTokenToLocal(accessToken)
                userDataRepository.setNewEmail(email)
                _loginUiState.value = LoginUiState.Success
            }
    }

    private suspend fun handleExistingUser(
        email: String,
        accessToken: String,
    ) {
        getUserTokenUseCase(email)
            .onStart { _loginUiState.value = LoginUiState.Loading }
            .catch { throwable ->
                sendErrorMessage(throwable)
            }
            .collectLatest {
                authRepository.saveEmailToLocal(email)
                authRepository.saveTokenToLocal(accessToken)
                _loginUiState.value = LoginUiState.Success
            }
    }

    fun onClickedKakaoLogin() {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState.LaunchLogin
        }
    }

    private fun isExistsEmail(message: String?): Boolean {
        return message?.contains(CONFLICT_CODE) == true
    }

    companion object {
        private const val CONFLICT_CODE = "HTTP 409"
    }
}
