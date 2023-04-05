package com.qure.data.datasource.geocoding

import com.qure.data.api.GeocodingService
import com.qure.data.entity.geocoding.GeocodingEntity
import javax.inject.Inject

class GeocodingRemoteRemoteDataSourceImpl @Inject constructor(
    private val geocodingService: GeocodingService
): GeocodingRemoteDataSource {

    override suspend fun getGeocoding(query: String): Result<GeocodingEntity> {
        return geocodingService.getGeocoding(query)
    }
}