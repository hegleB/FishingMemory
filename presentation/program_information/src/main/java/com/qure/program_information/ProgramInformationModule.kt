package com.qure.policy

import com.qure.navigator.ProgramInformationNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ProgramInformationModule {
    @Binds
    abstract fun bindProgramInformationNavigator(navigator: ProgramInformationNavigatorImpl): ProgramInformationNavigator
}
