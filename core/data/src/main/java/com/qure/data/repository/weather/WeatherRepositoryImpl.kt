package com.qure.data.repository.weather

import com.qure.data.datasource.weather.WeatherRemoteDataSource
import com.qure.data.mapper.toWeather
import com.qure.model.weather.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class WeatherRepositoryImpl
    @Inject
    constructor(
        private val weatherRemoteDataSource: WeatherRemoteDataSource,
    ) : WeatherRepository {
        override fun getWeather(
            base_date: Int,
            base_time: String,
            nx: String,
            ny: String,
        ): Flow<Weather> {
            return flow {
               emit(weatherRemoteDataSource.getWeather(base_date, base_time, nx, ny).toWeather())
            }
        }
    }
