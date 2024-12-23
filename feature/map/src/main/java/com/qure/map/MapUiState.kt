package com.qure.map

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.qure.model.map.MapType
import com.qure.model.map.MarkerType
import com.qure.model.toTedClusterItem
import com.qure.ui.model.FishingPlaceInfo
import com.qure.ui.model.MovingCameraWrapper
import com.qure.ui.model.SheetHeight
import com.qure.ui.model.toTedClusterItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import ted.gun0912.clustering.clustering.TedClusterItem

@Stable
sealed interface MapUiState {
    @Immutable
    data object Loading : MapUiState

    @Immutable
    data class Success(
        val fishingSpots: ImmutableList<FishingPlaceInfo.FishingSpotInfo> = persistentListOf(),
        val memos: ImmutableList<FishingPlaceInfo.MemoInfo> = persistentListOf(),
        val mapType: MapType = MapType.BASIC_MAP,
        val markerType: MarkerType = MarkerType.MEMO,
        val movingCameraState: MovingCameraWrapper = MovingCameraWrapper.Default,
        val sheetHeight: SheetHeight = SheetHeight.DEFAULT,
        val clusterMarkers: ImmutableList<TedClusterItem> = persistentListOf(),
    ) : MapUiState {
        val selectedPlaceItems = when (markerType) {
            MarkerType.MEMO -> {
                memos.filterToClusterMemoItems()
            }

            else -> {
                fishingSpots.filterToClusterSpotItems()
            }
        }

        private fun List<FishingPlaceInfo.MemoInfo>.filterToClusterMemoItems() = filter { memo ->
            clusterMarkers.any { item ->
                memo.memoUI.toTedClusterItem().getTedLatLng() == item.getTedLatLng()
            }
        }.toPersistentList()

        private fun List<FishingPlaceInfo.FishingSpotInfo>.filterToClusterSpotItems() = filter { spot ->
            clusterMarkers.any { item ->
                spot.fishingSpotUI.toTedClusterItem().getTedLatLng() == item.getTedLatLng()
            }
        }.toPersistentList()
    }
}