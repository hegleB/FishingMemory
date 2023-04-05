package com.qure.data.datasource.map

import com.qure.data.entity.map.GeocodingEntity

interface MapRemoteDataSource {
    suspend fun getGeocoding(
        query: String
    ): Result<GeocodingEntity>
}