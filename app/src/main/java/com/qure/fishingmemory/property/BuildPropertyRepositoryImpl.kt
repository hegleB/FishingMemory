package com.qure.fishingmemory.property

import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import com.qure.fishingmemory.BuildConfig
import timber.log.Timber
import javax.inject.Inject

class BuildPropertyRepositoryImpl @Inject constructor() : BuildPropertyRepository {

    override fun get(buildProperty: BuildProperty): String {
        return try {
            readBuildConfig(buildProperty = buildProperty)
        } catch (e: Exception) {
            throw IllegalStateException(
                "Failed to read property from local.properties. key: $buildProperty",
                e
            )
        }
    }

    override fun getOrNull(buildProperty: BuildProperty): String? {
        return try {
            readBuildConfig(buildProperty = buildProperty)
        } catch (e: Exception) {
            Timber.e(e, "Failed to read property from local.properties. key: $buildProperty")
            null
        }
    }

    private fun readBuildConfig(buildProperty: BuildProperty): String {
        return BuildConfig::class.java.getDeclaredField(buildProperty.key).get(null) as String
    }
}