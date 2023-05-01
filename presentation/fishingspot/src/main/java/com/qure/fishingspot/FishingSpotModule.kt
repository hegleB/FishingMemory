package com.qure.fishingspot

import com.qure.navigator.FishingSpotNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class FishingSpotModule {

    @Binds
    abstract fun bindFishingSpot(navigator: FishingSpotNavigatorImpl): FishingSpotNavigator
}