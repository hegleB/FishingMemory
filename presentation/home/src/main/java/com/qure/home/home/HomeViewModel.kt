package com.qure.home.home

import android.icu.util.LocaleData
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.core.extensions.Empty
import com.qure.domain.entity.weather.Item
import com.qure.domain.entity.weather.Items
import com.qure.domain.entity.weather.Weather
import com.qure.domain.entity.weather.WeatherCategory
import com.qure.domain.usecase.weather.GetWeatherUseCase
import com.qure.home.R
import com.qure.home.home.model.WeatherUI
import com.qure.home.home.model.toWeatherUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
) : BaseViewModel() {
    private val _UiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val UiState: StateFlow<UiState>
        get() = _UiState

    fun fetchWeater(latXLngY: LatXLngY) {
        viewModelScope.launch {
            getWeatherUseCase(
                base_date = getBaseDate(),
                base_time =  getBaseTime(),
                nx = latXLngY.nx.toInt().toString(),
                ny = latXLngY.ny.toInt().toString(),
            ).collect { response ->
                response.onSuccess { weather ->
                    _UiState.update {
                        it.copy(
                            weatherUI = weather.response.body.items.item.map { it.toWeatherUI() },
                            isWeatherInitialized = true
                        )
                    }
                }.onFailure {
                    Timber.d("weather failed ${it.message}")
                }
            }
        }
    }

    private fun getBaseTime(): String {
        val baseTime = "${LocalTime.now().hour - 1}30"
        Timber.d("baseTime : ${baseTime}")
        return baseTime
    }

    private fun getBaseDate(): Int {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val baseDate = LocalDate.now().format(formatter).toInt()
        Timber.d("baseDate : ${baseDate}")
        return baseDate
    }
}

data class UiState(
    val weatherUI: List<WeatherUI>? = null,
    val isWeatherInitialized: Boolean = false,
) {
    fun getWeahterState(): Int {
        val sky = getSkyState() ?: String.Empty
        val pty = getPrecipitationState() ?: String.Empty
        return when (pty.toInt()) {
            in listOf(
                1,
                2,
                5
            ) -> if (getHour() in 6..17) R.raw.weather_rainy_day else R.raw.weather_rainy_night
            in listOf(
                3,
                6,
                7
            ) -> if (getHour() in 6..17) R.raw.weather_snow_day else R.raw.weather_snow_night
            else -> getSky(sky.toInt())
        }
    }

    private fun getSky(sky: Int): Int {
        return when (sky) {
            in 0..5 -> if (getHour() in 6..17) R.raw.weather_sunny_day else R.raw.weather_sunny_night
            in 6..8 -> if (getHour() in 6..17) R.raw.weather_partly_cloudy_day else R.raw.weather_partly_cloudy_night
            else -> R.raw.weather_cloudey
        }
    }

    private fun getTemperatureState(): String? {
        return weatherUI?.filter { it.category == WeatherCategory.T1H }?.get(0)?.fcstValue
    }

    private fun getSkyState(): String? {
        return weatherUI?.filter { it.category == WeatherCategory.SKY }?.get(0)?.fcstValue
    }

    private fun getPrecipitationState(): String? {
        return weatherUI?.filter { it.category == WeatherCategory.PTY }?.get(0)?.fcstValue
    }

    private fun getThunder(): String? {
        return weatherUI?.filter { it.category == WeatherCategory.LGT }?.get(0)?.fcstValue
    }

    fun toTemperatureString(): String {
        return getTemperatureState() + "°"
    }

    fun toWeatherString(): String {
        return getSkyState() ?: String.Empty
    }


    private fun getHour(): Int {
        val now = System.currentTimeMillis()
        val currentTime = Date(now)
        val formatTime = SimpleDateFormat("HH")
        return formatTime.format(currentTime).toInt()
    }
}
