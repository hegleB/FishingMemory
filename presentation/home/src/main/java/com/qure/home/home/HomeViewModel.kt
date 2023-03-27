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
                base_time = getBaseTime(),
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
                    sendErrorMessage(it.message)
                }
            }
        }
    }

    private fun getBaseTime(): String {
        val baseTime = "${String.format("%02d", LocalTime.now().hour - 1)}30"
        return baseTime
    }

    private fun getBaseDate(): Int {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val baseDate = LocalDate.now().format(formatter).toInt()
        return baseDate
    }
}

data class UiState(
    val weatherUI: List<WeatherUI>? = null,
    val isWeatherInitialized: Boolean = false,
) {

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
        return weatherUI?.filter { it.category == WeatherCategory.T1H }?.get(0) ?: WeatherUI()
    }

    fun getSkyState(): WeatherUI {
        return weatherUI?.filter { it.category == WeatherCategory.SKY }?.get(0) ?: WeatherUI()
    }

    fun getPrecipitationState(): WeatherUI {
        return weatherUI?.filter { it.category == WeatherCategory.PTY }?.get(0) ?: WeatherUI()
    }

    fun getThunder(): WeatherUI {
        return weatherUI?.filter { it.category == WeatherCategory.LGT }?.get(0) ?: WeatherUI()
    }
}
