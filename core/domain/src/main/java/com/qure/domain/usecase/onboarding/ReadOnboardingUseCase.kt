package com.qure.domain.usecase.onboarding

import com.qure.data.repository.onboarding.OnboardingRepository
import com.qure.model.onboarding.OnboardingType
import javax.inject.Inject

class ReadOnboardingUseCase
    @Inject
    constructor(
        private val onboardingRepository: OnboardingRepository,
    ) {
        suspend operator fun invoke(onboardingType: OnboardingType): String? = onboardingRepository.readOnboarding(onboardingType.key)
    }
