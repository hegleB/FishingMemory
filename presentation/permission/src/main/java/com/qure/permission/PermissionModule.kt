package com.qure.permission

import com.qure.navigator.PermissionNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class PermissionModule {
    @Binds
    abstract fun bindPermissionNavigator(navigator: PermissionNavigatorImpl): PermissionNavigator
}
