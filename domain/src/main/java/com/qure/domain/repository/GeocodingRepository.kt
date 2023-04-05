package com.qure.domain.repository

import com.qure.domain.entity.geocoding.Geocoding
import kotlinx.coroutines.flow.Flow

interface GeocodingRepository {
    fun getGeocoding(
        query: String,
    ): Flow<Result<Geocoding>>
}