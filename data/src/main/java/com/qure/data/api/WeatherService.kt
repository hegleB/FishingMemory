package com.qure.data.api

import com.qure.data.entity.weather.WeatherEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("getUltraSrtFcst")
    suspend fun getWeather(
        @Query("base_date") baseDate : Int,
        @Query("base_time") baseTime : String,
        @Query("nx") nx : String,
        @Query("ny") ny : String
    ): Result<WeatherEntity>
}