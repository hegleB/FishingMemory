package com.qure.map

import androidx.lifecycle.viewModelScope
import com.qure.domain.usecase.fishingspot.GetFishingSpotUseCase
import com.qure.domain.usecase.memo.GetFilteredMemoUseCase
import com.qure.model.fishingspot.FishingSpot
import com.qure.model.map.MapType
import com.qure.model.map.MarkerType
import com.qure.model.memo.Memo
import com.qure.model.toFishingSpotUI
import com.qure.ui.base.BaseViewModel
import com.qure.ui.model.FishingPlaceInfo
import com.qure.ui.model.MovingCameraWrapper
import com.qure.ui.model.SheetHeight
import com.qure.ui.model.toMemoUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ted.gun0912.clustering.clustering.TedClusterItem
import javax.inject.Inject

@HiltViewModel
class MapViewModel
@Inject
constructor(
    private val getFishingSpotUseCase: GetFishingSpotUseCase,
    private val getFilteredMemoUseCase: GetFilteredMemoUseCase,
) : BaseViewModel() {

    private val _mapUiState = MutableStateFlow<MapUiState>(MapUiState.Loading)
    val mapUiState = _mapUiState.asStateFlow()

    init {
        fetchFilteredMemo()
    }

    private fun fetchFishingSpot(markerType: MarkerType) {
        viewModelScope.launch {
            getFishingSpotUseCase(FISHING_SPOT_COLLECTION_ID)
                .map { fishingSpots ->
                    createFishingSpotUiState(fishingSpots, markerType)
                }
                .catch { throwable -> sendErrorMessage(throwable) }
                .collectLatest { newState -> _mapUiState.value = newState }
        }
    }

    private fun createFishingSpotUiState(
        fishingSpots: List<FishingSpot>,
        markerType: MarkerType
    ): MapUiState {
        val filteredSpots = fishingSpots.filter {
            it.document.fields.fishing_ground_type.stringValue == markerType.value
        }.map { it.toFishingSpotUI() }.map { FishingPlaceInfo.FishingSpotInfo(it) }.toPersistentList()

        val currentState = _mapUiState.value
        return if (currentState is MapUiState.Success) {
            currentState.copy(fishingSpots = filteredSpots)
        } else {
            MapUiState.Success(fishingSpots = filteredSpots)
        }
    }

    private fun fetchFilteredMemo() {
        viewModelScope.launch {
            getFilteredMemoUseCase()
                .map { memos -> createMemoUiState(memos) }
                .catch { throwable -> sendErrorMessage(throwable) }
                .collectLatest { newState -> _mapUiState.value = newState }
        }
    }

    private fun createMemoUiState(memos: List<Memo>): MapUiState {
        val memoItems = memos.map { it.toMemoUI() }.map { FishingPlaceInfo.MemoInfo(it) }.toPersistentList()

        val currentState = _mapUiState.value
        return if (currentState is MapUiState.Success) {
            currentState.copy(memos = memoItems)
        } else {
            MapUiState.Success(memos = memoItems)
        }
    }

    fun setMapType(mapType: MapType) {
        _mapUiState.update { currentState ->
            (currentState as? MapUiState.Success)?.copy(
                mapType = mapType
            ) ?: currentState
        }
    }

    fun setMarkerType(markerType: MarkerType) {
        updateState { it.copy(markerType = markerType) }
        when (markerType) {
            MarkerType.MEMO -> fetchFilteredMemo()
            else -> fetchFishingSpot(markerType)
        }
    }

    fun updateMovingCamera(movingCameraWrapper: MovingCameraWrapper) {
        updateState { it.copy(movingCameraState = movingCameraWrapper) }
    }

    fun updateSheetHeight(sheetHeight: SheetHeight) {
        updateState { it.copy(sheetHeight = sheetHeight) }
    }

    fun updateClusterMarkers(clusterMarkers: List<TedClusterItem>) {
        updateState { it.copy(clusterMarkers = clusterMarkers.toPersistentList()) }
    }

    private fun updateState(update: (MapUiState.Success) -> MapUiState.Success) {
        _mapUiState.update { currentState ->
            if (currentState is MapUiState.Success) {
                update(currentState)
            } else {
                currentState
            }
        }
    }

    companion object {
        const val FISHING_SPOT_COLLECTION_ID = "fishingspot"
        var initialMarkerLoadFlag = true
    }
}
