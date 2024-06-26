package com.qure.fishingspot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.overlay.OverlayImage
import com.qure.designsystem.component.FMBackButton
import com.qure.designsystem.component.FMBookmarkButton
import com.qure.designsystem.component.FMFishingSpotItem
import com.qure.designsystem.component.FMNaverMap
import com.qure.designsystem.utils.FMPreview
import com.qure.model.FishingSpotUI
import kotlinx.coroutines.flow.collectLatest

@Composable
fun FishingSpotRoute(
    viewModel: FishingSpotViewModel = hiltViewModel(),
    fishingSpot: FishingSpotUI,
    onBack: () -> Unit,
    onClickPhoneNumber: (String) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {

    LaunchedEffect(viewModel.error) {
        viewModel.error.collectLatest(onShowErrorSnackBar)
    }

    LaunchedEffect(Unit) {
        viewModel.checkBookmark(fishingSpot.number)
    }

    FishingSpotScreen(
        fishingSpot = fishingSpot,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
            .background(color = MaterialTheme.colorScheme.background),
        onBack = onBack,
        onClickBookmark = { viewModel.toggleBookmarkButton(fishingSpot) },
        isBookmarkClicked = viewModel.isBookmarkClicked,
        onClickPhoneNumber = onClickPhoneNumber,
    )
}

@Composable
private fun FishingSpotScreen(
    fishingSpot: FishingSpotUI,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onClickBookmark: () -> Unit,
    isBookmarkClicked: Boolean,
    onClickPhoneNumber: (String) -> Unit,
) {

    Column(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            FMBackButton(
                onClickBack = { onBack() },
                iconColor = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = fishingSpot.fishing_spot_name,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
            )
            FMBookmarkButton(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.CenterEnd),
                onClick = { onClickBookmark() },
                isBookmarkClicked = isBookmarkClicked,
            )
        }

        val fishingSpotPosition = LatLng(fishingSpot.latitude, fishingSpot.longitude)
        val cameraPositionState: CameraPositionState = rememberCameraPositionState {
            position = CameraPosition(fishingSpotPosition, 14.0)
        }

        FMNaverMap(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(300.dp)
                .clip(RoundedCornerShape(25.dp)),
            cameraPositionState = cameraPositionState,
            markerState = MarkerState(position = fishingSpotPosition),
            markerHeight = 30.dp,
            markerWidth = 35.dp,
            icon = OverlayImage.fromResource(com.qure.core.designsystem.R.drawable.bg_map_fill_marker),
        )

        FMFishingSpotItem(
            fishingSpotName = fishingSpot.fishing_spot_name,
            fishingGroundType = fishingSpot.fishing_ground_type,
            numberAddress = fishingSpot.number_address,
            roadAddress = fishingSpot.road_address,
            phoneNumber = fishingSpot.phone_number,
            fishType = fishingSpot.fish_type,
            mainPoint = fishingSpot.main_point,
            fee = fishingSpot.fee,
            onClickPhoneNumber = onClickPhoneNumber,
        )
    }
}

@Preview
@Composable
private fun FishingSpotScreenPreview() = FMPreview {
    FishingSpotScreen(
        fishingSpot = FishingSpotUI(),
        onBack = { },
        onClickBookmark = { },
        isBookmarkClicked = false,
        onClickPhoneNumber = { },
    )
}
