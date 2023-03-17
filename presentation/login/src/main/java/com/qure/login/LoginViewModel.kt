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
    private val createUserUseCase: CreateUserUseCase
) : BaseViewModel() {

    private val _action = MutableSharedFlow<Action>()
    val action: SharedFlow<Action>
        get() = _action.asSharedFlow()

    fun createUser(email: String, userId: String) = viewModelScope.launch {
        kotlin.runCatching {
            createUserUseCase(
                email = email,
                userId = userId
            ).onSuccess { value: SignUpUser ->
                Timber.d("가입여부 ${value.isSignUp}")
                _action.emit(
                    if (value.isSignUp) Action.AlreadySignUp else Action.FirstSignUp
                )
            }.onFailure {throwable ->
                Timber.d("가입여부2 ${throwable}")
            }
        }.onFailure {
            Timber.d("가입여부3 ${it.message}")
        }
    }

    sealed class Action {
        object FirstSignUp : Action()
        object AlreadySignUp : Action()
    }
}