package com.qure.onboarding

import com.qure.navigator.OnboardingNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class OnboardingModule {
    @Binds
    abstract fun bindOnboardingNavigator(navigator: OnboardingNavigatorImpl): OnboardingNavigator
}
