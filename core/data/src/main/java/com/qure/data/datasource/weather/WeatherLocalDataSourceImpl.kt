package com.qure.data.datasource.weather

import com.qure.data.entity.weather.WeatherLocalEntity
import com.qure.data.local.WeatherDao
import javax.inject.Inject

internal class WeatherLocalDataSourceImpl @Inject constructor(
    private val weatherDao: WeatherDao,
) : WeatherLocalDataSource {
    override suspend fun getWeather(): List<WeatherLocalEntity> {
        return weatherDao.getWeather()
    }

    override suspend fun insertWeather(weatherLocalEntity: WeatherLocalEntity) {
        weatherDao.insertWeather(weatherLocalEntity)
    }

    override suspend fun deleteWeather() {
        weatherDao.getMinId()?.let { id ->
            weatherDao.deleteWeatherById(id)
        }
    }
}