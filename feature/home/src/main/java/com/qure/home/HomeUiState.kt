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
    }
}