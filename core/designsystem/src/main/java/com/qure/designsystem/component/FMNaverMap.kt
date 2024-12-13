package com.qure.designsystem.component

import android.graphics.Color
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationSource
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.DisposableMapEffect
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.qure.core.designsystem.R
import ted.gun0912.clustering.clustering.TedClusterItem
import ted.gun0912.clustering.naver.TedNaverClustering

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun FMNaverMap(
    modifier: Modifier = Modifier,
    locationSource: LocationSource = rememberFusedLocationSource(),
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    markerState: MarkerState = MarkerState(),
    markerHeight: Dp = 0.dp,
    markerWidth: Dp = 0.dp,
    icon: OverlayImage = OverlayImage.fromResource(R.drawable.bg_map_fill_marker),
    onMapClick: (LatLng) -> Unit = { },
    markers: List<TedClusterItem> = emptyList(),
    onClickMarker: (TedClusterItem) -> Unit = { },
    onClickClusterItems: (List<TedClusterItem>) -> Unit = { },
    isClusteringMarkers: Boolean = false,
    mapHeight: Dp = 0.dp,
    mapType: MapType = MapType.Basic,
    locationTrackingMode: LocationTrackingMode = LocationTrackingMode.None,
    onMapLoaded: () -> Unit = { },
) {
    NaverMap(
        modifier = modifier,
        locationSource = locationSource,
        cameraPositionState = cameraPositionState,
        onMapClick = { _, latLng ->
            onMapClick(latLng)
        },
        uiSettings = MapUiSettings(
            isZoomControlEnabled = false,
            isCompassEnabled = false,
            logoMargin = PaddingValues(
                start = 0.dp,
                end = 0.dp,
                top = 0.dp,
                bottom = mapHeight,
            )
        ),
        properties = MapProperties(
            mapType = mapType,
            locationTrackingMode = locationTrackingMode,
        ),
        onMapLoaded = onMapLoaded,
    ) {
        if (isClusteringMarkers) {
            val context = LocalContext.current

            var clusterManager by remember {
                mutableStateOf<TedNaverClustering<TedClusterItem>?>(null)
            }

            DisposableMapEffect(markers) { map ->
                if (clusterManager == null) {
                    clusterManager = TedNaverClustering.with<TedClusterItem>(context, map)
                        .customMarker { tedClusterItem -> tedClusterItem.createMarker() }
                        .clusterBackground {
                            Color.BLACK
                        }
                        .markerClickListener { marker ->
                            onClickMarker(marker)
                        }
                        .clusterClickListener { clusterItems ->
                            onClickClusterItems(clusterItems.items.toList())
                        }
                        .minClusterSize(1)
                        .make()
                }
                clusterManager?.addItems(markers)

                onDispose {
                    clusterManager?.clearItems()
                }
            }
        } else {
            Marker(
                state = markerState,
                height = markerHeight,
                width = markerWidth,
                icon = icon,
            )
        }
    }
}

private fun List<TedClusterItem>.toTedLatLng() =
    this.map { tedClusterItem -> tedClusterItem.getTedLatLng() }.sortedByDescending { it.latitude }

private fun TedClusterItem.createMarker(): Marker {
    val tedLatLng = getTedLatLng()
    val latLng = LatLng(tedLatLng.latitude, tedLatLng.longitude)
    return Marker(latLng).apply {
        icon =
            OverlayImage.fromResource(R.drawable.bg_map_fill_marker)
        width = 100
        height = 120
        isHideCollidedCaptions = true
        isHideCollidedSymbols = true
    }
}