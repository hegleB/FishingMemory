package com.qure.data.di

import com.qure.data.repository.auth.AuthRepository
import com.qure.data.repository.auth.AuthRepositoryImpl
import com.qure.data.repository.darkmode.DarkModeRepository
import com.qure.data.repository.darkmode.DarkModeRepositoryImpl
import com.qure.data.repository.fishingspot.FishingSpotLocalRepository
import com.qure.data.repository.fishingspot.FishingSpotLocalRepositoryImpl
import com.qure.data.repository.fishingspot.FishingSpotRepository
import com.qure.data.repository.fishingspot.FishingSpotRepositoryImpl
import com.qure.data.repository.map.MapRepository
import com.qure.data.repository.map.MapRepositoryImpl
import com.qure.data.repository.memo.MemoRepository
import com.qure.data.repository.memo.MemoRepositoryImpl
import com.qure.data.repository.onboarding.OnboardingRepository
import com.qure.data.repository.onboarding.OnboardingRepositoryImpl
import com.qure.data.repository.weather.WeatherRepository
import com.qure.data.repository.weather.WeatherRepositoryImpl
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
    abstract fun bindAuthRepository(repository: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(repository: OnboardingRepositoryImpl): OnboardingRepository

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(repository: WeatherRepositoryImpl): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindMapRepository(repository: MapRepositoryImpl): MapRepository

    @Binds
    @Singleton
    abstract fun bindMemoRepository(repository: MemoRepositoryImpl): MemoRepository

    @Binds
    @Singleton
    abstract fun bindFishingSpotRepository(repository: FishingSpotRepositoryImpl): FishingSpotRepository

    @Binds
    @Singleton
    abstract fun bindFishingSpotLocalRepository(repository: FishingSpotLocalRepositoryImpl): FishingSpotLocalRepository

    @Binds
    @Singleton
    abstract fun bindDarkModeRepository(repository: DarkModeRepositoryImpl): DarkModeRepository
}
