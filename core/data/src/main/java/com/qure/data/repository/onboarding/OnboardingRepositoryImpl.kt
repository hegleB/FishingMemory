package com.qure.data.repository.onboarding

import com.qure.data.datasource.datastore.DataStoreDataSource
import javax.inject.Inject

class OnboardingRepositoryImpl
    @Inject
    constructor(
        private val dataStoreDataSource: DataStoreDataSource,
    ) : OnboardingRepository {
        override suspend fun readOnboarding(key: String): String? {
            return dataStoreDataSource.readDataSource(key)
        }

        override suspend fun writeOnboarding(
            key: String,
            value: String,
        ) {
            dataStoreDataSource.writeDataSource(key = key, value = value)
        }
    }
