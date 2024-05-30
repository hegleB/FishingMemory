package com.qure.create.location

import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.create.model.toGeocodingUI
import com.qure.create.model.toReverseGeocodingUI
import com.qure.domain.usecase.map.GetGeocodingUseCase
import com.qure.domain.usecase.map.GetReverseGeocodingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationSettingViewModel
@Inject
constructor(
    private val getGeocodingUseCase: GetGeocodingUseCase,
    private val getReverseGeocodingUseCase: GetReverseGeocodingUseCase,
) : BaseViewModel() {
    private val _geoCodingUiState =
        MutableStateFlow<GeoCodingUiState>(GeoCodingUiState.Success())
    val geoCodingUiState = _geoCodingUiState.asStateFlow()

    private val _locationUiState = MutableStateFlow(LocationUiState())
    val locationUiState = _locationUiState.asStateFlow()

    fun fetchGeocoding(query: String) {
        viewModelScope.launch {
            getGeocodingUseCase(query)
                .map { geocoding -> GeoCodingUiState.Success(geocoding = geocoding.toGeocodingUI()) }
                .onStart { _geoCodingUiState.value = GeoCodingUiState.Loading }
                .catch { throwable -> sendErrorMessage(throwable) }
                .collectLatest { geocodingUiState ->
                    _geoCodingUiState.value = geocodingUiState
                    _locationUiState.update {
                        it.copy(geocoding = geocodingUiState.geocoding)
                    }
                }
        }
    }

    fun fetchReverseGeocoding(coords: String) {
        viewModelScope.launch {
            getReverseGeocodingUseCase(coords)
                .map { reverseGeocoding -> GeoCodingUiState.Success(reverseGeocoding = reverseGeocoding.toReverseGeocodingUI()) }
                .onStart { _geoCodingUiState.value = GeoCodingUiState.Loading }
                .catch { throwable -> sendErrorMessage(throwable) }
                .collectLatest { geocodingUiState ->
                    _geoCodingUiState.value = geocodingUiState
                    _locationUiState.update {
                        it.copy(reverseGeocoding = geocodingUiState.reverseGeocoding)
                    }
                }
        }
    }

    fun onClickNext() {
        if (_locationUiState.value.currentPage < 2) {
            _locationUiState.update {
                it.copy(currentPage = _locationUiState.value.currentPage.plus(1))
            }
        }
    }

    fun onClickPrevious() {
        if (_locationUiState.value.currentPage > 0) {
            _locationUiState.update {
                it.copy(currentPage = _locationUiState.value.currentPage.minus(1))
            }
        }
    }

    fun setDoIndexDate(doIndex: Int) {
        _locationUiState.update {
            it.copy(doIndex = doIndex)
        }
    }

    fun setCityIndexData(cityIndex: Int) {
        _locationUiState.update {
            it.copy(cityIndex = cityIndex)
        }
    }

    fun setRegionsData(regions: List<String>) {
        _locationUiState.update {
            it.copy(regions = regions)
        }
    }

    fun setSelectedRegionsData(selectedRegions: List<String>) {
        _locationUiState.update {
            it.copy(selectedRegions = selectedRegions)
        }
    }
}