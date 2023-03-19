package com.qure.data.repository

import com.qure.data.datasource.datasotre.DataStoreDataSource
import com.qure.domain.repository.OnboardingRepository
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val dataStoreDataSource: DataStoreDataSource
): OnboardingRepository {
    override suspend fun readOnboarding(key: String): String? {
        return dataStoreDataSource.readDataSource(key)
    }

    override suspend fun writeOnboarding(key: String, value: String) {
        dataStoreDataSource.writeDataSource(key = key, value = value)
    }
}