package com.qure.data.di

import com.qure.data.repository.*
import com.qure.domain.repository.*
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

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        repository: WeatherRepositoryImpl,
    ): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindMapRepository(
        repository: MapRepositoryImpl,
    ): MapRepository

    @Binds
    @Singleton
    abstract fun bindMemoRepository(
        repository: MemoRepositoryImpl,
    ): MemoRepository
}