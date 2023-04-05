package com.qure.data.di

import com.qure.data.repository.AuthRepositoryImpl
import com.qure.data.repository.GeocodingRepositoryImpl
import com.qure.data.repository.OnboardingRepositoryImpl
import com.qure.data.repository.WeatherRepositoryImpl
import com.qure.domain.repository.AuthRepository
import com.qure.domain.repository.GeocodingRepository
import com.qure.domain.repository.OnboardingRepository
import com.qure.domain.repository.WeatherRepository
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
    abstract fun bindGeocodingRepository(
        repository: GeocodingRepositoryImpl,
    ): GeocodingRepository
}