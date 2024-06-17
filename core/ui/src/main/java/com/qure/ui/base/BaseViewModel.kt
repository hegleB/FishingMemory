package com.qure.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qure.ui.model.SnackBarMessageType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    protected val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    protected val _error = MutableSharedFlow<Throwable>()
    val error: SharedFlow<Throwable>
        get() = _error

    protected val _message = MutableSharedFlow<SnackBarMessageType>()
    val message: SharedFlow<SnackBarMessageType>
        get()= _message

    fun startLoading() {
        _isLoading.value = true
    }

    fun stopLoading() {
        _isLoading.value = false
    }

    fun sendErrorMessage(throwable: Throwable) {
        viewModelScope.launch {
            _error.emit(throwable)
        }
    }

    fun sendMessage(messageType: SnackBarMessageType) {
        viewModelScope.launch {
            _message.emit(messageType)
        }
    }
}
