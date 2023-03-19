package com.qure.domain.usecase.onboarding

import com.qure.domain.entity.OnboardingType
import com.qure.domain.repository.OnboardingRepository
import javax.inject.Inject

class WriteOnboardingUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
){
    suspend operator fun invoke(onboardingType: OnboardingType) {
        onboardingRepository.writeOnboarding(key = onboardingType.key, value = "IS_SHOWN")
    }
}