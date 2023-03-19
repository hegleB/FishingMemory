package com.qure.data.di

import com.qure.data.datasource.FishMemorySharedPreference
import com.qure.data.datasource.FishMemorySharedPreferenceImpl
import com.qure.data.datasource.datasotre.DataStoreDataSource
import com.qure.data.datasource.datasotre.DataStoreDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalModule {

    @Binds
    @Singleton
    abstract fun bindSharedPrefernce(
        fishMemorySharedPreferenceImpl: FishMemorySharedPreferenceImpl
    ): FishMemorySharedPreference

    @Binds
    @Singleton
    abstract fun bindDataStoreDataSource(
        dataStoreDataSource: DataStoreDataSourceImpl
    ): DataStoreDataSource
}