package com.qure.memo

import com.qure.navigator.MemoNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class MemoModule {
    @Binds
    abstract fun bindMemoNavigator(navigator: MemoNavigatorImpl): MemoNavigator
}
