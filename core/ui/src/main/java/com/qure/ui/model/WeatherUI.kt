package com.qure.ui.model

import android.annotation.SuppressLint
import com.qure.core.designsystem.R
import com.qure.model.weather.Item
import com.qure.model.weather.WeatherCategory
import java.text.SimpleDateFormat
import java.util.Date

data class WeatherUI(
    val baseDate: Int = 0,
    val baseTime: Int = 0,
    val category: WeatherCategory = WeatherCategory.UNKNOWN,
    val fcstDate: Int = 0,
    val fcstTime: Int = 0,
    val fcstValue: String = "",
    val nx: Int = 0,
    val ny: Int = 0,
    val checkedWeather: Int = if (isDayTime()) R.raw.weather_sunny_day else R.raw.weather_sunny_night,
)

data class HourOfWeatherState(
    val skyState: WeatherUI,
    val temperature: WeatherUI,
    val precipitationState: WeatherUI,
    val thunder: WeatherUI,
) {

    fun toTemperatureString(): String {
        return "${temperature.checkedWeather}°"
    }
    fun toWeatherAnimation(): Int {
        return when {
            thunder.fcstValue.toInt() > 1 -> thunder.checkedWeather
            precipitationState.fcstValue.toInt() > 0 -> precipitationState.checkedWeather
            else -> skyState.checkedWeather
        }
    }

    fun convertToHourString(): String {
        val hour = this.temperature.fcstTime / 100
        return String.format("%d시", hour)
    }

    fun toCurrentDateTimeWeather(): String {
        return "${convertToDate(this.temperature.fcstDate)}\n${convertToAmPm(this.temperature.fcstTime)} 기준"
    }

    private fun convertToDate(date: Int): String {
        val month = (date % 10000) / 100
        val day = date % 100
        return String.format("%02d/%02d", month, day)
    }

    private fun convertToAmPm(time: Int): String {
        val hour = time / 100
        val minute = time % 100
        val period = if (hour >= 12) "오후" else "오전"
        val adjustedHour = if (hour % 12 == 0) 12 else hour % 12
        return String.format("%s %d:%02d", period, adjustedHour, minute)
    }
}

fun List<WeatherUI>.categorizeWeather(): List<HourOfWeatherState> {
    if (isEmpty()) return emptyList()
    val skyStates = this.filter { it.category == WeatherCategory.SKY }
    val temperatures = this.filter { it.category == WeatherCategory.T1H }
    val precipitationStates = this.filter { it.category == WeatherCategory.PTY }
    val thunders = this.filter { it.category == WeatherCategory.LGT }
    val minSize = minOf(skyStates.size, temperatures.size, precipitationStates.size, thunders.size)
    return List(minSize.minus(1)) { index ->
        HourOfWeatherState(
            skyState = skyStates[index],
            temperature = temperatures[index],
            precipitationState = precipitationStates[index],
            thunder = thunders[index]
        )
    }
}

fun Item.toWeatherUI(): WeatherUI {
    return WeatherUI(
        baseDate = this.baseDate,
        baseTime = this.baseTime,
        category = this.category,
        fcstDate = this.fcstDate,
        fcstTime = this.fcstTime,
        fcstValue = this.fcstValue,
        nx = this.nx,
        ny = this.ny,
        checkedWeather =
        when (this.category) {
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
        in listOf(
            1,
            2,
            5
        ) -> if (isDayTime()) R.raw.weather_rainy_day else R.raw.weather_rainy_night

        in listOf(3, 6, 7) -> if (isDayTime()) R.raw.weather_snow_day else R.raw.weather_snow_night
        else -> 0
    }
}

private fun getSky(sky: Int): Int {
    return when (sky) {
        SkyState.SUNNY.number -> if (isDayTime()) R.raw.weather_sunny_day else R.raw.weather_sunny_night
        SkyState.CLOUDY.number -> if (isDayTime()) R.raw.weather_partly_cloudy_day else R.raw.weather_partly_cloudy_night
        else -> R.raw.weather_cloudey
    }
}

@SuppressLint("SimpleDateFormat")
private fun isDayTime(): Boolean {
    val now = System.currentTimeMillis()
    val currentTime = Date(now)
    val formatTime = SimpleDateFormat("HH")
    return formatTime.format(currentTime).toInt() in 6..17
}

enum class SkyState(val number: Int) {
    SUNNY(1),
    CLOUDY(3),
}
