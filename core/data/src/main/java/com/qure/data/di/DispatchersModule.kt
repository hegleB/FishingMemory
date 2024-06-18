package com.qure.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DispatcherIO

    @Provides
    @DispatcherIO
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}