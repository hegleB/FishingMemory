package com.qure.data.repository.onboarding

interface OnboardingRepository {
    suspend fun readOnboarding(key: String): String?

    suspend fun writeOnboarding(
        key: String,
        value: String,
    )
}
