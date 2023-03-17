package com.qure.data.di

import com.qure.data.datasource.auth.AuthRemoteDataSource
import com.qure.data.datasource.auth.AuthRemoteDataSourceImpl
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
}