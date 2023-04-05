package com.qure.data.api

import com.qure.data.entity.geocoding.GeocodingEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingService {
    @GET("v2/geocode")
    suspend fun getGeocoding(
        @Query("query") query: String,
    ): Result<GeocodingEntity>
}