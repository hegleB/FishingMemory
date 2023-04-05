package com.qure.data.datasource.geocoding

import com.qure.data.entity.geocoding.GeocodingEntity

interface GeocodingRemoteDataSource {
    suspend fun getGeocoding(
        query: String
    ): Result<GeocodingEntity>
}