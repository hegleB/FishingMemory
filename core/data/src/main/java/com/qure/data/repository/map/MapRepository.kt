package com.qure.data.repository.map

import com.qure.model.map.Geocoding
import com.qure.model.map.ReverseGeocoding
import kotlinx.coroutines.flow.Flow

interface MapRepository {
    fun getGeocoding(query: String): Flow<Geocoding>

    fun getReverseGeocoding(coords: String): Flow<ReverseGeocoding>
}
