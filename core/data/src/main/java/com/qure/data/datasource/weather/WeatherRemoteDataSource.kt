package com.qure.data.datasource.weather

import com.qure.data.entity.weather.WeatherEntity

internal interface WeatherRemoteDataSource {
    suspend fun getWeather(
        baseDate: Int,
        baseTime: String,
        nx: String,
        ny: String,
    ): WeatherEntity
}
