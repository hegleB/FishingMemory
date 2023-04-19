package com.qure.memo.detail

import com.qure.navigator.DetailMemoNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DetailMemoModule {

    @Binds
    abstract fun bindDetailMemoNavigator(navigator: DetailMemoNavigatorImpl): DetailMemoNavigator
}