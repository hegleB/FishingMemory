package com.qure.onboarding

import androidx.lifecycle.viewModelScope
import com.qure.domain.usecase.onboarding.WriteOnboardingUseCase
import com.qure.model.onboarding.OnboardingType
import com.qure.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel
    @Inject
    constructor(
        private val writeOnboardingUseCase: WriteOnboardingUseCase,
    ) : BaseViewModel() {
        fun writeFirstVisitor() =
            viewModelScope.launch {
                writeOnboardingUseCase(OnboardingType.AFTER_SPLASH)
            }
    }
