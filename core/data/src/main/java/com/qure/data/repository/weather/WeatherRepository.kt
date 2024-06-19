package com.qure.data.repository.weather

import com.qure.data.entity.weather.WeatherLocalEntity
import com.qure.model.weather.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeather(
        baseDate: Int,
        baseTime: String,
        nx: String,
        ny: String,
    ): Flow<Weather>
    suspend fun insertWeather(weatherLocalEntity: WeatherLocalEntity)

    suspend fun deleteWeather()
}
