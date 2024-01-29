package com.qure.data.datasource.weather

import com.qure.data.entity.weather.WeatherEntity

interface WeatherRemoteDataSource {
    suspend fun getWeather(
        base_date: Int,
        base_time: String,
        nx: String,
        ny: String,
    ): Result<WeatherEntity>
}
