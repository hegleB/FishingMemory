package com.qure.login

import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.domain.entity.auth.SignUpUser
import com.qure.domain.usecase.auth.CreateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
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
                    _action.emit(Action.AlreadySignUp)
                }
            }
        }.onFailure {
            it as Exception
            if (isExistsEmail(it.message)) {
                _action.emit(Action.AlreadySignUp)
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