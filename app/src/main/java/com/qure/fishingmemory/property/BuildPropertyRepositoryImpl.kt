package com.qure.fishingmemory.property

import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import com.qure.fishingmemory.BuildConfig
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class BuildPropertyRepositoryImpl
    @Inject
    constructor() : BuildPropertyRepository {
        override fun get(buildProperty: BuildProperty): String {
            return try {
                readBuildConfig(buildProperty = buildProperty)
            } catch (e: IOException) {
                throw IllegalStateException(
                    "Failed to read property from local.properties. key: $buildProperty",
                    e,
                )
            }
        }

        override fun getOrNull(buildProperty: BuildProperty): String? {
            return try {
                readBuildConfig(buildProperty = buildProperty)
            } catch (e: IOException) {
                Timber.e(e, "Failed to read property from local.properties. key: $buildProperty")
                null
            }
        }

        private fun readBuildConfig(buildProperty: BuildProperty): String {
            return BuildConfig::class.java.getDeclaredField(buildProperty.key).get(null) as String
        }
    }
