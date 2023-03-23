package com.qure.domain.repository

import com.qure.domain.entity.weather.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeather(
        base_date: Int,
        base_time: String,
        nx: String,
        ny: String
    ): Flow<Result<Weather>>
}