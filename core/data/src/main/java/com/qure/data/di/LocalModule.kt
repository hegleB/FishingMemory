package com.qure.data.di

import com.qure.data.datasource.FishMemorySharedPreference
import com.qure.data.datasource.FishMemorySharedPreferenceImpl
import com.qure.data.datasource.datastore.DataStoreDataSource
import com.qure.data.datasource.datastore.DataStoreDataSourceImpl
import com.qure.data.datasource.fishingspot.FishingSpotLocalDataSource
import com.qure.data.datasource.fishingspot.FishingSpotLocalDataSourceImpl
import com.qure.data.datasource.memo.MemoLocalDataSource
import com.qure.data.datasource.memo.MemoLocalDataSourceImpl
import com.qure.data.datasource.weather.WeatherLocalDataSource
import com.qure.data.datasource.weather.WeatherLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocalModule {
    @Binds
    @Singleton
    abstract fun bindSharedPrefernce(fishMemorySharedPreferenceImpl: FishMemorySharedPreferenceImpl): FishMemorySharedPreference

    @Binds
    @Singleton
    abstract fun bindDataStoreDataSource(dataStoreDataSource: DataStoreDataSourceImpl): DataStoreDataSource

    @Binds
    @Singleton
    abstract fun bindFishingSpotLocalDataSource(dataStoreDataSource: FishingSpotLocalDataSourceImpl): FishingSpotLocalDataSource

    @Binds
    @Singleton
    abstract fun bindMemoLocalDataSource(dataStoreDataSource: MemoLocalDataSourceImpl): MemoLocalDataSource

    @Binds
    @Singleton
    abstract fun bindWeatherLocalDataSource(dataStoreDataSource: WeatherLocalDataSourceImpl): WeatherLocalDataSource
}
