package com.qure.domain.repository

interface OnboardingRepository {
    suspend fun readOnboarding(key: String): String?

    suspend fun writeOnboarding(
        key: String,
        value: String,
    )
}
