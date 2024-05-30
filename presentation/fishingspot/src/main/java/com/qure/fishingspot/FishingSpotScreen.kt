package com.qure.fishingspot

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.overlay.OverlayImage
import com.qure.core_design.compose.components.FMBackButton
import com.qure.core_design.compose.components.FMBookmarkButton
import com.qure.core_design.compose.components.FMFishingSpotItem
import com.qure.core_design.compose.components.FMNaverMap
import com.qure.core_design.compose.theme.Gray300
import com.qure.core_design.compose.utils.FMPreview
import com.qure.model.FishingSpotUI

@Composable
fun FishingSpotScreen(
    fishingSpot: FishingSpotUI,
    viewModel: FishingSpotViewModel,
    onBack: () -> Unit,
    onClickBookmark: () -> Unit,
    onClickPhoneNumber: (String) -> Unit,
) {

    FishingSpotContent(
        fishingSpot = fishingSpot,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
            .background(color = MaterialTheme.colorScheme.background),
        onBack = onBack,
        onClickBookmark = onClickBookmark,
        isBookmarkClicked = viewModel.isBookmarkClicked,
        onClickPhoneNumber = onClickPhoneNumber,
    )
}

@Composable
private fun FishingSpotContent(
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
            icon = OverlayImage.fromResource(com.qure.core_design.R.drawable.bg_map_fill_marker),
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
    FishingSpotContent(
        fishingSpot = FishingSpotUI(),
        onBack = { },
        onClickBookmark = { },
        isBookmarkClicked = false,
        onClickPhoneNumber = { },
    )
}
