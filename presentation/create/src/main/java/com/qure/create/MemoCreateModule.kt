package com.qure.create

import com.qure.navigator.MemoCreateNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class MemoCreateModule {
    @Binds
    abstract fun bindMemoCreateNavigator(navigator: MemoCreateNavigatorImpl): MemoCreateNavigator
}
