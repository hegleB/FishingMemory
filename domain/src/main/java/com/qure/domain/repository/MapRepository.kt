package com.qure.domain.repository

import com.qure.domain.entity.map.Geocoding
import com.qure.domain.entity.map.ReverseGeocoding
import kotlinx.coroutines.flow.Flow

interface MapRepository {
    fun getGeocoding(
        query: String,
    ): Flow<Result<Geocoding>>

    fun getReverseGeocoding(
        coords: String,
    ): Flow<Result<ReverseGeocoding>>
}