package com.qure.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String>
        get() = _error

    protected fun startLoading() {
        _isLoading.value = true
    }

    protected fun stopLoading() {
        _isLoading.value = false
    }

    protected fun sendErrorMessage(throwable: Throwable) {
        sendErrorMessage(throwable.message)
    }

    protected fun sendErrorMessage(errorMessage: String?) {
        viewModelScope.launch {
            _error.emit(errorMessage ?: "알 수 없는 오류가 발생했습니다.")
        }
    }
}