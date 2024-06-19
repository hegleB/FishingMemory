package com.qure.home

import com.qure.model.weather.WeatherCategory

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data object Empty : HomeUiState
    data class Success(
        val weather: List<com.qure.ui.model.WeatherUI> = emptyList(),
        val memos: List<com.qure.ui.model.MemoUI> = emptyList(),
    ) : HomeUiState {
        fun toTemperatureString(): String {
            return "${getTemperatureState().checkedWeather}°"
        }

        fun toWeatherAnimation(): Int {
            when {
                getThunder().fcstValue.toInt() > 1 -> return getThunder().checkedWeather
                getPrecipitationState().fcstValue.toInt() > 0 -> return getPrecipitationState().checkedWeather
                else -> return getSkyState().checkedWeather
            }
        }

        fun getTemperatureState(): com.qure.ui.model.WeatherUI {
            return weather.filter { it.category == WeatherCategory.T1H }[0]
        }

        fun getSkyState(): com.qure.ui.model.WeatherUI {
            return weather.filter { it.category == WeatherCategory.SKY }[0]
        }

        fun getPrecipitationState(): com.qure.ui.model.WeatherUI {
            return weather.filter { it.category == WeatherCategory.PTY }[0]
        }

        fun getThunder(): com.qure.ui.model.WeatherUI {
            return weather.filter { it.category == WeatherCategory.LGT }[0]
        }

        fun toCurrentDateTimeWeather(): String {
            return "${convertToDate(weather.first().fcstDate)}\n${convertToAmPm(weather.first().fcstTime)} 기준"
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
}