package com.qure.domain.usecase.weather

import com.qure.data.repository.weather.WeatherRepository
import com.qure.model.weather.Weather
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherUseCase
    @Inject
    constructor(
        private val weatherRepository: WeatherRepository,
    ) {
        operator fun invoke(
            baseDate: Int,
            baseTime: String,
            nx: String,
            ny: String,
        ): Flow<Weather> = weatherRepository.getWeather(baseDate, baseTime, nx, ny)
    }
