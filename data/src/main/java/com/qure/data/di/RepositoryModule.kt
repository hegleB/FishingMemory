package com.qure.data.di

import com.qure.data.repository.AuthRepositoryImpl
import com.qure.data.repository.OnboardingRepositoryImpl
import com.qure.domain.repository.AuthRepository
import com.qure.domain.repository.OnboardingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        repository: AuthRepositoryImpl,
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(
        repository: OnboardingRepositoryImpl,
    ): OnboardingRepository
}