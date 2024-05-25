package com.qure.home.home

import com.qure.domain.entity.weather.WeatherCategory
import com.qure.home.home.model.WeatherUI
import com.qure.memo.model.MemoUI

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data object Empty : HomeUiState
    data class Success(
        val weather: List<WeatherUI> = emptyList(),
        val memos: List<MemoUI> = emptyList(),
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

        fun getTemperatureState(): WeatherUI {
            return weather.filter { it.category == WeatherCategory.T1H }[0]
        }

        fun getSkyState(): WeatherUI {
            return weather.filter { it.category == WeatherCategory.SKY }[0]
        }

        fun getPrecipitationState(): WeatherUI {
            return weather.filter { it.category == WeatherCategory.PTY }[0]
        }

        fun getThunder(): WeatherUI {
            return weather.filter { it.category == WeatherCategory.LGT }[0]
        }
    }
}