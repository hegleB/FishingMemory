package com.qure.data.datasource.weather

import com.qure.data.entity.weather.WeatherLocalEntity

internal interface WeatherLocalDataSource {
    suspend fun getWeather(): List<WeatherLocalEntity>
    suspend fun insertWeather(weatherLocalEntity: WeatherLocalEntity)

    suspend fun deleteWeather()
}
