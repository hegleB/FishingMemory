package com.qure.data.datasource.map

import com.qure.data.entity.map.GeocodingEntity
import com.qure.data.entity.map.ReverseGeocodingEntity

internal interface MapRemoteDataSource {
    suspend fun getGeocoding(query: String): GeocodingEntity

    suspend fun getReverseGeocoding(coords: String): ReverseGeocodingEntity
}
