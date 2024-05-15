package com.qure.create.location

import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.create.R
import com.qure.create.model.GeocodingUI
import com.qure.create.model.ReverseGeocodingUI
import com.qure.create.model.toGeocodingUI
import com.qure.create.model.toReverseGeocodingUI
import com.qure.domain.usecase.map.GetGeocodingUseCase
import com.qure.domain.usecase.map.GetReverseGeocodingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState>
        get() = _uiState

    private val _uiEffect: MutableSharedFlow<UiEffect> = MutableSharedFlow()
    val uiEffect: SharedFlow<UiEffect>
        get() = _uiEffect

    fun getGeocoding(query: String) {
        viewModelScope.launch {
            getGeocodingUseCase(query).collect { response ->
                response.onSuccess { geocoding ->
                    if (geocoding.status == "OK") {
                        _uiState.update {
                            it.copy(
                                geocodingUI = geocoding.toGeocodingUI(),
                                isGeocodingInitialized = true,
                            )
                        }
                    }
                }.onFailure { throwable ->
                    throwable as Exception
                }
            }
        }
    }

    fun getReverseGeocoding(coords: String) {
        viewModelScope.launch {
            getReverseGeocodingUseCase(coords).collect { response ->
                response.onSuccess { reverseGeocoding ->
                    if (reverseGeocoding.status.code == 0) {
                        _uiState.update {
                            it.copy(
                                reverseGeocodingUI = reverseGeocoding.toReverseGeocodingUI(),
                                isReverseGeocodingInitialized = true,
                                geocodingUI = GeocodingUI(coords = coords),
                            )
                        }
                    } else {
                        onFailMarkerClick()
                    }
                }.onFailure { throwable ->
                    throwable as Exception
                }
            }
        }
    }

    fun onFailMarkerClick() {
        viewModelScope.launch {
            _uiEffect.emit(
                UiEffect.ShowToastMessage(
                    resId = R.string.location_marker_failure_message,
                ),
            )
        }
    }

    fun onClickNext() {
        val currentPage = _uiState.value.currentPage
        if (currentPage < 2) {
            _uiState.update {
                it.copy(currentPage = currentPage.plus(1))
            }
        }
    }

    fun onClickPrevious() {
        val currentPage = _uiState.value.currentPage
        if (currentPage > 0) {
            _uiState.update {
                it.copy(currentPage = currentPage.minus(1))
            }
        }
    }

    fun setDoIndex(index: Int) {
        _uiState.update {
            it.copy(doIndex = index)
        }
    }

    fun setCityIndex(index: Int) {
        _uiState.update {
            it.copy(cityIndex = index)
        }
    }

    fun setRegions(regions: List<String>) {
        _uiState.update {
            it.copy(regions = regions)
        }
    }

    fun setSelectedRegions(regions: List<String>) {
        _uiState.update {
            it.copy(selectedRegions = regions)
        }
    }
}

data class UiState(
    val geocodingUI: GeocodingUI? = null,
    val reverseGeocodingUI: ReverseGeocodingUI? = null,
    val isGeocodingInitialized: Boolean = false,
    val isReverseGeocodingInitialized: Boolean = false,
    val currentPage: Int = 0,
    val doIndex: Int = -1,
    val cityIndex: Int = -1,
    val regions: List<String> = emptyList(),
    val selectedRegions: List<String> = List(3) { "" },
)

sealed interface UiEffect {
    data class ShowToastMessage(val resId: Int) : UiEffect
}
