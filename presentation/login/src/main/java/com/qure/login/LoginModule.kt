package com.qure.login

import com.qure.navigator.LoginNavigator
import com.qure.navigator.OnboardingNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LoginModule {

    @Binds
    abstract fun bindLoginNavigator(navigator: LoginNavigatorImpl): LoginNavigator
}