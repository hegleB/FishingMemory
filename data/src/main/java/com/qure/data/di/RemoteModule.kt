package com.qure.data.di

import com.qure.data.datasource.auth.AuthRemoteDataSource
import com.qure.data.datasource.auth.AuthRemoteDataSourceImpl
import com.qure.data.datasource.map.MapRemoteDataSource
import com.qure.data.datasource.map.MapRemoteRemoteDataSourceImpl
import com.qure.data.datasource.memo.MemoRemoteDataSource
import com.qure.data.datasource.memo.MemoRemoteDataSourceImpl
import com.qure.data.datasource.weather.WeatherRemoteDataSource
import com.qure.data.datasource.weather.WeatherRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteModule {

    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataResource(
        dataSource: AuthRemoteDataSourceImpl,
    ): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindWeatherRemoteDataResource(
        dataSource: WeatherRemoteDataSourceImpl,
    ): WeatherRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindMapRemoteDataResource(
        dataSource: MapRemoteRemoteDataSourceImpl,
    ): MapRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindMemoRemoteDataResource(
        dataSource: MemoRemoteDataSourceImpl,
    ): MemoRemoteDataSource
}