package com.qure.map

import com.qure.navigator.MapNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class MapModule {
    @Binds
    abstract fun bindMapNavigator(navigator: MapNavigatorImpl): MapNavigator
}
