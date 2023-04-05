package com.qure.data.datasource.map

import com.qure.data.api.NaverMapService
import com.qure.data.entity.map.GeocodingEntity
import javax.inject.Inject

class MapRemoteRemoteDataSourceImpl @Inject constructor(
    private val naverMapService: NaverMapService
): MapRemoteDataSource {

    override suspend fun getGeocoding(query: String): Result<GeocodingEntity> {
        return naverMapService.getGeocoding(query)
    }
}