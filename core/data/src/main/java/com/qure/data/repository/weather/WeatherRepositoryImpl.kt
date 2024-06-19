package com.qure.data.repository.weather

import com.qure.data.datasource.weather.WeatherLocalDataSource
import com.qure.data.datasource.weather.WeatherRemoteDataSource
import com.qure.data.entity.weather.WeatherLocalEntity
import com.qure.data.mapper.toWeather
import com.qure.data.utils.NetworkMonitor
import com.qure.model.weather.Weather
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

internal class WeatherRepositoryImpl @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val networkMonitor: NetworkMonitor,
) : WeatherRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getWeather(
        baseDate: Int,
        baseTime: String,
        nx: String,
        ny: String,
    ): Flow<Weather> {
        return networkMonitor.isConnectNetwork.flatMapLatest { isConnectNetwork ->
            if (isConnectNetwork) {
                fetchRemoteWeather(baseDate, baseTime, nx, ny)
            } else {
                fetchLocalWeather()
            }
        }
    }

    private fun fetchRemoteWeather(
        baseDate: Int,
        baseTime: String,
        nx: String,
        ny: String,
    ): Flow<Weather> = flow {
        val remoteWeather =
            weatherRemoteDataSource.getWeather(baseDate, baseTime, nx, ny).toWeather()
        emit(remoteWeather)
    }.onEach { weather ->
        val localWeather = weatherLocalDataSource.getWeather()
        if (localWeather.size >= MAX_WEATHER_SIZE) {
            deleteWeather()
        }
        insertWeather(WeatherLocalEntity(response = weather.response))
    }

    private fun fetchLocalWeather(): Flow<Weather> = flow {
        if (weatherLocalDataSource.getWeather().isNotEmpty()) {
            emit(weatherLocalDataSource.getWeather().last().toWeather())
        } else {
            emit(Weather.EMPTY)
        }
    }

    override suspend fun insertWeather(weatherLocalEntity: WeatherLocalEntity) {
        weatherLocalDataSource.insertWeather(weatherLocalEntity)
    }

    override suspend fun deleteWeather() {
        weatherLocalDataSource.deleteWeather()
    }

    companion object {
        private const val MAX_WEATHER_SIZE = 5
    }
}
