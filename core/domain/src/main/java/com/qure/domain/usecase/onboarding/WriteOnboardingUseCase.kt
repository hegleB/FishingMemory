package com.qure.domain.usecase.onboarding

import com.qure.data.repository.onboarding.OnboardingRepository
import com.qure.model.onboarding.OnboardingType
import javax.inject.Inject

class WriteOnboardingUseCase
    @Inject
    constructor(
        private val onboardingRepository: OnboardingRepository,
    ) {
        suspend operator fun invoke(onboardingType: OnboardingType) {
            onboardingRepository.writeOnboarding(key = onboardingType.key, value = "IS_SHOWN")
        }
    }
