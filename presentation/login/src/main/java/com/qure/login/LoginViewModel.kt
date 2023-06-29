package com.qure.login

import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.core.extensions.Colon
import com.qure.data.datasource.FishMemorySharedPreference
import com.qure.domain.ACCESS_TOKEN_KEY
import com.qure.domain.entity.auth.SignUpUser
import com.qure.domain.repository.AuthRepository
import com.qure.domain.usecase.auth.CreateUserUseCase
import com.qure.domain.usecase.auth.GetUserTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val getUserTokenUseCase: GetUserTokenUseCase,
    private val fishingSharedPreference: FishMemorySharedPreference,
    private val authRepository: AuthRepository,
) : BaseViewModel() {

    private val _action = MutableSharedFlow<Action>()
    val action: SharedFlow<Action>
        get() = _action.asSharedFlow()

    fun createUser(email: String, accessToken: String) = viewModelScope.launch {
        kotlin.runCatching {
            createUserUseCase(
                email = email,
                socialToken = accessToken
            ).onSuccess { value: SignUpUser ->
                _action.emit(Action.FirstSignUp)
            }.onFailure { throwable ->
                if (isExistsEmail(throwable.message)) {
                    saveToLocalSignedUpUser(email)
                    _action.emit(Action.AlreadySignUp)
                } else {
                    sendErrorMessage(throwable.message)
                }
            }
        }.onFailure {
            it as Exception
            if (isExistsEmail(it.message)) {
                saveToLocalSignedUpUser(email)
                _action.emit(Action.AlreadySignUp)
            } else {
                sendErrorMessage(it.message)
            }
        }
    }

    private fun saveToLocalSignedUpUser(email: String) = viewModelScope.launch {
        authRepository.saveEmailToLocal(email)
        getUserTokenUseCase(email = email).collect { response ->
            response.onSuccess { user ->
                fishingSharedPreference.putString(ACCESS_TOKEN_KEY, user.fields.token.stringValue)
            }.onFailure { throwable ->
                throwable as Exception
                sendErrorMessage(throwable.message)
            }
        }
    }

    private fun isExistsEmail(message: String?): Boolean {
        val documentMessage = message?.split(String.Colon) ?: emptyList()
        return documentMessage[0] == EMAIL_EXISTS
    }

    sealed class Action {
        object FirstSignUp : Action()
        object AlreadySignUp : Action()
    }

    companion object {
        private const val EMAIL_EXISTS = "Document already exists"
    }
}