package com.qure.core_design.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.overlay.OverlayImage
import com.qure.core_design.R

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun FMNaverMap(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    markerState: MarkerState = MarkerState(),
    markerHeight: Dp = 0.dp,
    markerWidth: Dp = 0.dp,
    icon: OverlayImage = OverlayImage.fromResource(R.drawable.bg_map_fill_marker),
    onMapClick: (LatLng) -> Unit = { },
) {
    NaverMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        onMapClick = { _, latLng ->
            onMapClick(latLng)
        }
    ) {
        Marker(
            state = markerState,
            height = markerHeight,
            width = markerWidth,
            icon = icon,
        )
    }
}