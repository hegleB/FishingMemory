package com.qure.onboarding

import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.domain.entity.OnboardingType
import com.qure.domain.usecase.onboarding.WriteOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val writeOnboardingUseCase: WriteOnboardingUseCase,
): BaseViewModel() {

    fun writeFirstVisitor() = viewModelScope.launch {
        writeOnboardingUseCase(OnboardingType.AFTER_SPLASH)
    }
}