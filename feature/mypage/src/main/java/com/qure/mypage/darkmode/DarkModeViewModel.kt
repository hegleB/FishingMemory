package com.qure.mypage.darkmode

import androidx.lifecycle.viewModelScope
import com.qure.data.repository.user.UserDataRepository
import com.qure.data.utils.THEME_DARK
import com.qure.data.utils.THEME_SYSTEM
import com.qure.domain.usecase.darkmode.GetDarkModeUseCase
import com.qure.domain.usecase.darkmode.SetDarkModeUseCase
import com.qure.model.darkmode.DarkModeConfig
import com.qure.model.extensions.Empty
import com.qure.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DarkModeViewModel
@Inject
constructor(
    private val setDarkModeUseCase: SetDarkModeUseCase,
    private val getDarkModeUseCase: GetDarkModeUseCase,
    private val userDataRepository: UserDataRepository,
) : BaseViewModel() {
    private val _currentThemeMode: MutableStateFlow<String> = MutableStateFlow(String.Empty)
    val currentThemeMode = _currentThemeMode.asStateFlow()

    init {
        getDarkMode()
    }

    fun setDarkMode(selectedDarMode: String) {
        viewModelScope.launch {
            setDarkModeUseCase(selectedDarMode)
            userDataRepository.setDarkModeConfig(when(selectedDarMode) {
                THEME_DARK -> DarkModeConfig.DARK
                THEME_SYSTEM -> DarkModeConfig.SYSTEM
                else -> DarkModeConfig.LIGHT
            })
            _currentThemeMode.value = selectedDarMode
        }
    }

    fun getDarkMode() {
        viewModelScope.launch {
           _currentThemeMode.value = getDarkModeUseCase()
        }
    }
}
