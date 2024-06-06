package com.qure.home

import androidx.lifecycle.viewModelScope
import com.qure.domain.usecase.memo.GetFilteredMemoUseCase
import com.qure.domain.usecase.weather.GetWeatherUseCase
import com.qure.home.location.LatXLngY
import com.qure.model.extensions.DefaultLatitude
import com.qure.model.extensions.DefaultLongitude
import com.qure.model.extensions.twoDigitsFormat
import com.qure.ui.base.BaseViewModel
import com.qure.ui.model.toMemoUI
import com.qure.ui.model.toWeatherUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getFilteredMemoUseCase: GetFilteredMemoUseCase,
) : BaseViewModel() {

    private val _selectedChip = MutableStateFlow(INITIAL_FISH_TYPE)
    val selectedChip = _selectedChip.asStateFlow()
    private val _latLng = MutableStateFlow(
        LatXLngY(
            String.DefaultLatitude.toDouble(),
            String.DefaultLongitude.toDouble(),
        )
    )
    val latLng = _latLng.asStateFlow()

    private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeUiState = _homeUiState.asStateFlow()

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            combine(
                getWeatherUseCase(
                    base_date = getBaseDate(),
                    base_time = getBaseTime(),
                    nx = _latLng.value.nx.toInt().toString(),
                    ny = _latLng.value.ny.toInt().toString(),
                ),
                getFilteredMemoUseCase(),
                ::Pair
            )
                .map { ui ->
                    HomeUiState.Success(
                        weather = ui.first.response.body?.items?.item?.map { it.toWeatherUI() } ?: emptyList(),
                        memos = ui.second.map { it.toMemoUI() }
                    )
                }
                .onStart { _homeUiState.value = HomeUiState.Loading }
                .catch { throwable -> sendErrorMessage(throwable) }
                .collectLatest { homeUiState ->
                    _homeUiState.value = homeUiState
                }
        }
    }


    fun setSelectedChip(chip: String) {
        _selectedChip.value = chip
    }

    fun setLatLng(latXLngY: LatXLngY) {
        _latLng.value = latXLngY
    }

    private fun getBaseTime(): String {
        val baseTime = "${(LocalTime.now().hour - 1).twoDigitsFormat()}30"
        return baseTime
    }

    private fun getBaseDate(): Int {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val baseDate = LocalDate.now().format(formatter).toInt()
        return baseDate
    }

    companion object {
        private const val INITIAL_FISH_TYPE = "어종"
    }
}