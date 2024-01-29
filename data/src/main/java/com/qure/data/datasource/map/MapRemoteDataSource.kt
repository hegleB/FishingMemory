package com.qure.data.datasource.map

import com.qure.data.entity.map.GeocodingEntity
import com.qure.data.entity.map.ReverseGeocodingEntity

interface MapRemoteDataSource {
    suspend fun getGeocoding(query: String): Result<GeocodingEntity>

    suspend fun getReverseGeocoding(coords: String): Result<ReverseGeocodingEntity>
}
