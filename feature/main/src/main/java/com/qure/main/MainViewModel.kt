package com.qure.main

import androidx.lifecycle.viewModelScope
import com.qure.data.repository.user.UserDataRepository
import com.qure.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val userDataRepository: UserDataRepository,
) : BaseViewModel() {
    val userData: StateFlow<MainUiState> = userDataRepository.userData
        .map { MainUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MainUiState.Loading
        )

    fun setKakaoDeepLink(isKakaoOpenLink: Boolean) {
        viewModelScope.launch {
            userDataRepository.setKakaoDeepLink(isKakaoOpenLink)
        }
    }
}
