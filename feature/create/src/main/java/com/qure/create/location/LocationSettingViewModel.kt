package com.qure.create.location

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.qure.domain.usecase.map.GetGeocodingUseCase
import com.qure.domain.usecase.map.GetReverseGeocodingUseCase
import com.qure.ui.base.BaseViewModel
import com.qure.ui.model.toGeocodingUI
import com.qure.ui.model.toReverseGeocodingUI
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
        MutableStateFlow<GeoCodingUiState>(GeoCodingUiState.Idle)
    val geoCodingUiState = _geoCodingUiState.asStateFlow()

    private val _reverseGeoCodingUiState =
        MutableStateFlow<ReverseGeoCodingUiState>(ReverseGeoCodingUiState.Idle)
    val reverseGeoCodingUiState = _reverseGeoCodingUiState.asStateFlow()

    private val _locationUiState = MutableStateFlow(LocationUiState())
    val locationUiState = _locationUiState.asStateFlow()

    private val _selectedRegionName = mutableStateListOf<String>()
    val selectedRegionName: List<String>
        get() = _selectedRegionName

    private var currentPage = 0

    private val _doIndex = mutableIntStateOf(-1)
    val doIndex: Int
        get() = _doIndex.intValue

    private val _cityIndex = mutableIntStateOf(-1)
    val cityIndex: Int
        get() = _cityIndex.intValue

    fun fetchGeocoding(query: String) {
        viewModelScope.launch {
            getGeocodingUseCase(query)
                .map { geocoding -> GeoCodingUiState.Success(geocoding = geocoding.toGeocodingUI()) }
                .onStart { _geoCodingUiState.value = GeoCodingUiState.Loading }
                .catch { throwable -> sendErrorMessage(throwable) }
                .collectLatest { geocodingUiState ->
                    _geoCodingUiState.value = geocodingUiState
                }
        }
    }

    fun fetchReverseGeocoding(coords: String) {
        viewModelScope.launch {
            getReverseGeocodingUseCase(coords)
                .map { reverseGeocoding -> ReverseGeoCodingUiState.Success(reverseGeocoding = reverseGeocoding.toReverseGeocodingUI()) }
                .onStart { _reverseGeoCodingUiState.value = ReverseGeoCodingUiState.Loading }
                .catch { throwable -> sendErrorMessage(throwable) }
                .collectLatest { reverseGeocodingUiState ->
                    _reverseGeoCodingUiState.value = reverseGeocodingUiState
                }
        }
    }

    fun onClickNext() {
        if (_doIndex.intValue == Regions.entries.lastIndex) {
            currentPage += 2
        } else {
            if (currentPage < 3) {
                currentPage += 1
            }
        }
        if (_cityIndex.intValue != -1 && currentPage == 1) {
            val region = RegionData.regions[_doIndex.intValue].subRegions[_cityIndex.intValue]
            _selectedRegionName.add(if (region == NO_REGION_NAME) "" else region)
        }
        _locationUiState.update {
            it.copy(
                currentPage = currentPage,
                region = Regions.entries[_doIndex.intValue]
            )
        }
    }

    fun onClickPrevious() {
        if (_doIndex.intValue == Regions.entries.lastIndex) {
            currentPage -= 2
        } else {
            if (currentPage > 0) {
                currentPage -= 1
            }
        }
        when (currentPage) {
            0 -> if (_selectedRegionName.size == 2) {
                _selectedRegionName.removeAt(_selectedRegionName.lastIndex)
            }


            1 -> if (_selectedRegionName.size == 3) {
                _selectedRegionName.removeAt(_selectedRegionName.lastIndex)
            }
        }
        val region = when {
            currentPage > 0 -> Regions.entries[_doIndex.intValue]
            else -> Regions.REGION
        }
        _locationUiState.update {
            it.copy(
                currentPage = currentPage,
                region = region,
            )
        }
    }

    fun setDoIndexData(doIndex: Int) {
        if (doIndex != _doIndex.value) {
            _doIndex.intValue = doIndex
            _cityIndex.intValue = -1
            _selectedRegionName.clear()
        }
    }

    fun setCityIndexData(cityIndex: Int) {
        if (_cityIndex.intValue != -1) {
            _selectedRegionName.removeLast()
        }
        _cityIndex.intValue = cityIndex
    }

    fun setRegionName(regionName: String) {
        when {
            _selectedRegionName.size == 3 -> {
                _selectedRegionName.removeLast()
            }
            _doIndex.intValue == Regions.entries.lastIndex -> {
                val (doName, cityName, addressNumber) = regionName.split(" ", limit = 3).let {
                    Triple(it[0], it[1], it.drop(2).joinToString(" "))
                }
                _selectedRegionName.apply {
                    add(doName)
                    add(cityName)
                    add(addressNumber)
                }
            }
            regionName != NO_REGION_NAME -> {
                _selectedRegionName.add(regionName)
            }
        }
    }

    companion object {
        private const val NO_REGION_NAME = "없음"
    }
}

enum class Regions {
    SEOUL,
    BUSAN,
    DAEGU,
    INCHEON,
    GWANGJU,
    DAEJEON,
    ULSAN,
    SEJONG,
    GYEONGGI,
    GANGWON,
    CHUNGBUK,
    CHUNGNAM,
    GYEONGBUK,
    GYEONGNAM,
    JEONBUK,
    JEONNAM,
    JEJU,
    REGION,
    CURRENT_LOCATION,
}