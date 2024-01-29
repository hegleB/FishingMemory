package com.qure.fishingmemory.property

import com.qure.build_property.BuildPropertyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BuildPropertyModule {
    @Provides
    @Singleton
    fun provideBuildPropertyRepository(buildPropertyRepositoryImpl: BuildPropertyRepositoryImpl): BuildPropertyRepository =
        buildPropertyRepositoryImpl
}
