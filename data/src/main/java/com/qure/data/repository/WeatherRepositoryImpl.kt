package com.qure.data.repository

import com.qure.data.datasource.weather.WeatherRemoteDataSource
import com.qure.data.entity.weather.WeatherEntity
import com.qure.data.mapper.toWeather
import com.qure.domain.entity.weather.Weather
import com.qure.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource
) : WeatherRepository {
    override fun getWeather(
        base_date: Int,
        base_time: String,
        nx: String,
        ny: String
    ): Flow<Result<Weather>> {
        return flow {
            weatherRemoteDataSource.getWeather(base_date, base_time, nx, ny)
                .onSuccess { weather ->
                    emit(Result.success(weather.toWeather()))
                }.onFailure { throwable ->
                    emit(Result.failure(throwable))
                }
        }
    }
}