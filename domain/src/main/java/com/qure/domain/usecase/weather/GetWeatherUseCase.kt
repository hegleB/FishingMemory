package com.qure.domain.usecase.weather

import com.qure.domain.entity.weather.Weather
import com.qure.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherUseCase
    @Inject
    constructor(
        private val weatherRepository: WeatherRepository,
    ) {
        operator fun invoke(
            base_date: Int,
            base_time: String,
            nx: String,
            ny: String,
        ): Flow<Result<Weather>> = weatherRepository.getWeather(base_date, base_time, nx, ny)
    }
