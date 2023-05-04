package com.qure.fishingspot.bookmark

import com.qure.navigator.BookmarkNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BookmarkModule {

    @Binds
    abstract fun bindBookmark(navigator: BookmarkNavigatorImpl): BookmarkNavigator
}