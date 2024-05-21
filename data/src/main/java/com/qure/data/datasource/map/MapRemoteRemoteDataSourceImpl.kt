package com.qure.data.datasource.map

import com.qure.data.api.NaverMapService
import com.qure.data.entity.map.GeocodingEntity
import com.qure.data.entity.map.ReverseGeocodingEntity
import javax.inject.Inject

class MapRemoteRemoteDataSourceImpl
    @Inject
    constructor(
        private val naverMapService: NaverMapService,
    ) : MapRemoteDataSource {
        override suspend fun getGeocoding(query: String): GeocodingEntity {
            return naverMapService.getGeocoding(query)
        }

        override suspend fun getReverseGeocoding(coords: String): ReverseGeocodingEntity {
            return naverMapService.getReverseGeocoding(coords)
        }
    }
