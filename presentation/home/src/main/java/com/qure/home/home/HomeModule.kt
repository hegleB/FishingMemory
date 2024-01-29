package com.qure.home.home

import com.qure.navigator.HomeNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class HomeModule {
    @Binds
    abstract fun bindHomeNavigator(navigator: HomeNavigatorImpl): HomeNavigator
}
