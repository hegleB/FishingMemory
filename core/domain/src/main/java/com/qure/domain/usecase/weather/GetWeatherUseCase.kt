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
            base_date: Int,
            base_time: String,
            nx: String,
            ny: String,
        ): Flow<Weather> = weatherRepository.getWeather(base_date, base_time, nx, ny)
    }
