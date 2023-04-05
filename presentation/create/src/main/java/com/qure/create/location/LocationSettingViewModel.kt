package com.qure.create.location

import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.create.model.GeocodingUI
import com.qure.create.model.toGeocodingUI
import com.qure.domain.usecase.geocoding.GetGeocodingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationSettingViewModel @Inject constructor(
    private val getGeocodingUseCase: GetGeocodingUseCase,
) : BaseViewModel() {
    private val _UiState = MutableStateFlow(UiState())
    val UiState: StateFlow<UiState>
        get() = _UiState

    fun getGeocoding(query: String) {
        viewModelScope.launch {
            getGeocodingUseCase(query).collect { response ->
                response.onSuccess { geocoding ->
                    _UiState.update {
                        it.copy(
                            geocodingUI = geocoding.addresses?.map { it.toGeocodingUI() },
                            isGeocodingInitialized = true
                        )
                    }

                }
            }
        }
    }
}

data class UiState(
    val geocodingUI: List<GeocodingUI>? = null,
    val isGeocodingInitialized: Boolean = false,
)