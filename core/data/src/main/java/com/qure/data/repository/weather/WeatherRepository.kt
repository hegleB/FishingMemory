package com.qure.data.repository.weather

import com.qure.model.weather.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeather(
        base_date: Int,
        base_time: String,
        nx: String,
        ny: String,
    ): Flow<Weather>
}
