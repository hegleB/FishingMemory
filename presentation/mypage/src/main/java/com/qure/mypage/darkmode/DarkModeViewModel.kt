package com.qure.mypage.darkmode

import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.core.extensions.Empty
import com.qure.domain.usecase.darkmode.GetDarkModeUseCase
import com.qure.domain.usecase.darkmode.SetDarkModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DarkModeViewModel
@Inject
constructor(
    private val setDarkModeUseCase: SetDarkModeUseCase,
    private val getDarkModeUseCase: GetDarkModeUseCase,
) : BaseViewModel() {
    private val _currentThemeMode: MutableStateFlow<String> = MutableStateFlow(String.Empty)
    val currentThemeMode: StateFlow<String>
        get() = _currentThemeMode

    init {
        getDarkMode()
    }

    fun setDarkMode(selectedDarMode: String) {
        viewModelScope.launch {
            setDarkModeUseCase(selectedDarMode)
            _currentThemeMode.value = selectedDarMode
        }
    }

    fun getDarkMode() {
        viewModelScope.launch {
            _currentThemeMode.value = getDarkModeUseCase()
        }
    }
}
