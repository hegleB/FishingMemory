package com.qure.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    protected fun startLoading() {
        _isLoading.value = true
    }

    protected fun stopLoading() {
        _isLoading.value = false
    }
}