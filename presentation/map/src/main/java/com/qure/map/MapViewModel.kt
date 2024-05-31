package com.qure.map

import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.domain.entity.MapType
import com.qure.domain.entity.MarkerType
import com.qure.domain.entity.fishingspot.FishingSpotQuery
import com.qure.domain.entity.fishingspot.StructuredQuery
import com.qure.domain.entity.fishingspot.Where
import com.qure.domain.entity.memo.CollectionId
import com.qure.domain.entity.memo.FieldFilter
import com.qure.domain.entity.memo.FieldPath
import com.qure.domain.entity.memo.Value
import com.qure.domain.repository.AuthRepository
import com.qure.domain.usecase.fishingspot.GetFishingSpotUseCase
import com.qure.domain.usecase.memo.GetFilteredMemoUseCase
import com.qure.memo.model.toMemoUI
import com.qure.model.toFishingSpotUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel
@Inject
constructor(
    private val getFishingSpotUseCase: GetFishingSpotUseCase,
    private val getFilteredMemoUseCase: GetFilteredMemoUseCase,
    private val authRepository: AuthRepository,
) : BaseViewModel() {

    private val _mapType = MutableStateFlow(MapType.BASIC_MAP)
    val mapType = _mapType.asStateFlow()

    private val _markerType = MutableStateFlow(MarkerType.MEMO)
    val markerType = _markerType.asStateFlow()

    private val _placeItems = MutableStateFlow<List<FishingPlaceInfo>>(emptyList())
    val placeItems = _placeItems.asStateFlow()

    private val _selectedPlaceItems = MutableStateFlow<List<FishingPlaceInfo>>(emptyList())
    val selectedPlaceItems = _selectedPlaceItems.asStateFlow()

    private val _mapUiState = MutableStateFlow<MapUiState>(MapUiState.Loading)
    val mapUiState = _mapUiState.asStateFlow()
    init {
        fetchFilteredMemo()
    }

    fun fetchFishingSpot(fishingGroundType: MarkerType) {
        viewModelScope.launch {
            getFishingSpotUseCase(getStructuredQuery(fishingGroundType.value))
                .map { fishingSpots ->
                    MapUiState.Success(fishingSpots = fishingSpots.map { fishingSpot ->
                        FishingPlaceInfo.FishingSpotInfo(fishingSpot.toFishingSpotUI())
                    })
                }
                .onStart { _mapUiState.value = MapUiState.Loading }
                .catch { throwable -> sendErrorMessage(throwable) }
                .collectLatest { mapUiState ->
                    _mapUiState.value = mapUiState
                }
        }
    }

    fun fetchFilteredMemo() {
        viewModelScope.launch {
            getFilteredMemoUseCase()
                .map { memos ->
                    MapUiState.Success(memos = memos.map { memo ->
                        FishingPlaceInfo.MemoInfo(memo.toMemoUI())
                    })
                }
                .onStart { _mapUiState.value = MapUiState.Loading }
                .catch { throwable -> sendErrorMessage(throwable) }
                .collectLatest { mapUiState ->
                    _mapUiState.value = mapUiState
                }
        }
    }

    fun setMapType(mapType: MapType) {
        _mapType.value = mapType
    }

    fun setMarkerType(markerType: MarkerType) {
        _markerType.value = markerType
    }

    fun setPlaceItems(placeItems: List<FishingPlaceInfo>) {
        _placeItems.value = placeItems
    }

    fun setSelectedPlaceItems(placeItems: List<FishingPlaceInfo>) {
        _selectedPlaceItems.value = placeItems
    }

    private fun getStructuredQuery(fishingGroundType: String): FishingSpotQuery {
        val fieldFilter =
            FieldFilter(
                op = "EQUAL",
                field = FieldPath("fishing_ground_type"),
                value = Value(fishingGroundType),
            )

        return FishingSpotQuery(
            StructuredQuery(
                from = listOf(CollectionId(FISHING_SPOT_COLLECTION_ID)),
                where = Where(fieldFilter),
            ),
        )
    }

    companion object {
        const val FISHING_SPOT_COLLECTION_ID = "fishingspot"
    }
}
