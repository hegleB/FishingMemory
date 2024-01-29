package com.qure.data.api

import com.qure.data.entity.map.GeocodingEntity
import com.qure.data.entity.map.ReverseGeocodingEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverMapService {
    @GET("/map-geocode/v2/geocode")
    suspend fun getGeocoding(
        @Query("query") query: String,
    ): Result<GeocodingEntity>

    @GET("/map-reversegeocode/v2/gc")
    suspend fun getReverseGeocoding(
        @Query("coords") coords: String,
        @Query("orders") orders: String = "addr",
        @Query("output") output: String = "json",
    ): Result<ReverseGeocodingEntity>
}
