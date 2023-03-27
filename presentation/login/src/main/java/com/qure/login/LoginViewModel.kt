package com.qure.login

import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.data.datasource.FishMemorySharedPreference
import com.qure.domain.ACCESS_TOKEN_KEY
import com.qure.domain.SHARED_PREFERNCE_KEY
import com.qure.domain.entity.auth.SignUpUser
import com.qure.domain.usecase.auth.CreateUserUseCase
import com.qure.domain.usecase.auth.GetUserTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val getUserTokenUseCase: GetUserTokenUseCase,
    private val fishingSharedPreference: FishMemorySharedPreference,
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
        val documentMessage = message?.split(":") ?: emptyList()
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