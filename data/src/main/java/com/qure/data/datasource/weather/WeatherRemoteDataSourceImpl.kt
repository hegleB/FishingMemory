package com.qure.data.datasource.weather

import com.qure.data.api.WeatherService
import com.qure.data.entity.weather.WeatherEntity
import javax.inject.Inject

class WeatherRemoteDataSourceImpl
    @Inject
    constructor(
        private val weatherService: WeatherService,
    ) : WeatherRemoteDataSource {
        override suspend fun getWeather(
            base_date: Int,
            base_time: String,
            nx: String,
            ny: String,
        ): WeatherEntity {
            return weatherService.getWeather(
                baseDate = base_date,
                baseTime = base_time,
                nx = nx,
                ny = ny,
            )
        }
    }
