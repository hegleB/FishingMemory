package com.qure.data.datasource.weather

import com.qure.data.api.WeatherService
import com.qure.data.entity.weather.WeatherEntity
import javax.inject.Inject

internal class WeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherService: WeatherService,
) : WeatherRemoteDataSource {
    override suspend fun getWeather(
        baseDate: Int,
        baseTime: String,
        nx: String,
        ny: String,
    ): WeatherEntity {
        return weatherService.getWeather(
            baseDate = baseDate,
            baseTime = baseTime,
            nx = nx,
            ny = ny,
        )
    }
}
