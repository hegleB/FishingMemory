package com.qure.home.home.model

import com.qure.core.extensions.Empty
import com.qure.domain.entity.weather.Item
import com.qure.domain.entity.weather.WeatherCategory
import com.qure.home.R
import java.text.SimpleDateFormat
import java.util.*

data class WeatherUI(
    val baseData: Int = 0,
    val baseTime: Int = 0,
    val category: WeatherCategory = WeatherCategory.UNKNOWN,
    val fcstDate: Int = 0,
    val fcstTime: Int = 0,
    val fcstValue: String = String.Empty,
    val nx: Int = 0,
    val ny: Int = 0,
    val checkedWeather: Int = if (isDayTime()) R.raw.weather_sunny_day else R.raw.weather_sunny_night
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
            WeatherCategory.T1H -> this.fcstValue.toInt()
            WeatherCategory.LGT -> R.raw.weather_thunder
            WeatherCategory.SKY -> getSky(this.fcstValue.toInt())
            WeatherCategory.PTY -> getPrecipitationState(this.fcstValue.toInt())
            else -> 0
        },
    )
}

fun getPrecipitationState(pty: Int): Int {
    return when (pty) {
        in listOf(1, 2, 5)-> if (isDayTime()) R.raw.weather_rainy_day else R.raw.weather_rainy_night
        in listOf(3, 6, 7) -> if (isDayTime()) R.raw.weather_snow_day else R.raw.weather_snow_night
        else -> 0
    }
}


private fun getSky(sky: Int): Int {
    return when (sky) {
        SKY_STATE.SUNNY.nubmer -> if (isDayTime()) R.raw.weather_sunny_day else R.raw.weather_sunny_night
        SKY_STATE.CLOUDY.nubmer -> if (isDayTime()) R.raw.weather_partly_cloudy_day else R.raw.weather_partly_cloudy_night
        else -> R.raw.weather_cloudey
    }
}

private fun isDayTime(): Boolean {
    val now = System.currentTimeMillis()
    val currentTime = Date(now)
    val formatTime = SimpleDateFormat("HH")
    return formatTime.format(currentTime).toInt() in 6..17
}

enum class SKY_STATE(val nubmer: Int) {
    SUNNY(1),
    CLOUDY(3),
}