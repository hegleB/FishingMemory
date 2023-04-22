package com.qure.home.home

import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.domain.entity.memo.*
import com.qure.domain.entity.weather.WeatherCategory
import com.qure.domain.repository.AuthRepository
import com.qure.domain.usecase.memo.GetFilteredMemoUseCase
import com.qure.domain.usecase.weather.GetWeatherUseCase
import com.qure.home.home.model.WeatherUI
import com.qure.home.home.model.toWeatherUI
import com.qure.memo.model.MemoUI
import com.qure.memo.model.toMemoUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getFilteredMemoUseCase: GetFilteredMemoUseCase,
    private val authRepository: AuthRepository,
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
                            isWeatherInitialized = true,
                        )
                    }
                }.onFailure {
                    sendErrorMessage(it.message)
                }
            }
        }
    }

    fun getFilteredMemo() {
        viewModelScope.launch {
            getFilteredMemoUseCase(
                getStructuredQuery()
            ).collect { response ->
                response.onSuccess { result ->
                    _UiState.update {
                        it.copy(
                            isFilterInitialized = true,
                            filteredMemo = result.map { it.toMemoUI() }
                        )
                    }
                }.onFailure { throwable ->
                    sendErrorMessage(throwable)
                }
            }
        }
    }

    private fun getStructuredQuery(): MemoQuery {
        val emailFilter = FieldFilter(
            field = FieldPath(EMAIL),
            op = EQUAL,
            value = Value(authRepository.getEmailFromLocal())
        )

        val compositeFilter = CompositeFilter(
            op = AND,
            filters = listOf(Filter(emailFilter))
        )

        return MemoQuery(
            StructuredQuery(
                from = listOf(CollectionId(COLLECTION_ID)),
                where = Where(compositeFilter),
                orderBy = listOf(OrderBy(FieldPath(DATE), DESCENDING))
            )
        )
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

    companion object {
        private const val EMAIL = "email"
        private const val DATE = "date"
        private const val DESCENDING = "DESCENDING"
        private const val EQUAL = "EQUAL"
        private const val AND = "AND"
        private const val COLLECTION_ID = "memo"
    }
}

data class UiState(
    val weatherUI: List<WeatherUI>? = null,
    val isWeatherInitialized: Boolean = false,
    val isFilterInitialized: Boolean = false,
    val filteredMemo: List<MemoUI> = emptyList(),
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
