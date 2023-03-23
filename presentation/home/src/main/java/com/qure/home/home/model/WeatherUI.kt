package com.qure.home.home.model

import com.qure.domain.entity.weather.Item
import com.qure.domain.entity.weather.WeatherCategory
import com.qure.home.R

data class WeatherUI(
    val baseData: Int,
    val baseTime: Int,
    val category: WeatherCategory,
    val fcstDate: Int,
    val fcstTime: Int,
    val fcstValue: String,
    val nx: Int,
    val ny: Int,
    val checkedWeather: Int,
)

fun Item.toWeatherUI(): WeatherUI {
    return WeatherUI(
        baseData = this.baseData,
        baseTime = this.baseTime,
        category = this.category,
        fcstDate = this.fcstDate,
        fcstTime = this.fcstTime,
        fcstValue = this.fcstValue,
        nx = this.nx,
        ny = this.ny,
        checkedWeather = when (this.category) {
            WeatherCategory.T1H -> R.raw.weather_cloudey
            WeatherCategory.LGT -> R.raw.weather_thunder
            WeatherCategory.SKY -> R.raw.weather_cloudey
            WeatherCategory.PTY -> R.raw.weather_rainy_night
            else -> 0
        }
    )
}