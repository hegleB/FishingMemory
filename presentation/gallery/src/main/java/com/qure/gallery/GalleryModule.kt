package com.qure.gallery

import com.qure.navigator.GalleryNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class GalleryModule {
    @Binds
    abstract fun bindGalleryNavigator(navigator: GalleryNavigatorImpl): GalleryNavigator
}