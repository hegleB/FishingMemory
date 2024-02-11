package com.qure.fishingspot

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.overlay.OverlayImage
import com.qure.core_design.compose.components.FMBackButton
import com.qure.core_design.compose.components.FMBookmarkButton
import com.qure.core_design.compose.theme.Blue600
import com.qure.core_design.compose.theme.Gray300
import com.qure.core_design.compose.theme.Gray500
import com.qure.core_design.compose.utils.FMPreview
import com.qure.model.FishingSpotUI

@Composable
fun FishingSpotScreen(
    fishingSpot: FishingSpotUI,
    viewModel: FishingSpotViewModel,
    onBack: () -> Unit,
    onClickBookmark: () -> Unit,
    onClickPhoneNumber: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FishingSpotContent(
        fishingSpot = fishingSpot,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
            .background(color = MaterialTheme.colorScheme.background),
        onBack = onBack,
        onClickBookmark = onClickBookmark,
        isBookmarkClicked = uiState.isBookmarkClicked,
        onClickPhoneNumber = onClickPhoneNumber,
    )
}

data class FishingSpotData(
    @DrawableRes val iconRes: Int?,
    @StringRes val type: Int,
    val description: String,
    val onClickPhoneNumber: () -> Unit = { },
)

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun FishingSpotContent(
    fishingSpot: FishingSpotUI,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onClickBookmark: () -> Unit,
    isBookmarkClicked: Boolean,
    onClickPhoneNumber: () -> Unit,
) {
    val fishingSpotData = listOf(
        FishingSpotData(
            iconRes = com.qure.core_design.R.drawable.ic_empty_marker,
            type = R.string.number_address,
            description = fishingSpot.number_address,
        ),
        FishingSpotData(
            iconRes = null,
            type = R.string.road_address,
            description = fishingSpot.road_address,
        ),
        FishingSpotData(
            iconRes = com.qure.core_design.R.drawable.ic_phone_number,
            type = R.string.phone_number,
            description = fishingSpot.phone_number,
        ),
        FishingSpotData(
            iconRes = com.qure.core_design.R.drawable.ic_fish,
            type = R.string.fish_type,
            description = fishingSpot.fish_type,
        ),
        FishingSpotData(
            iconRes = com.qure.core_design.R.drawable.ic_won,
            type = R.string.fee,
            description = fishingSpot.fee,
        ),
        FishingSpotData(
            iconRes = com.qure.core_design.R.drawable.ic_fishing,
            type = R.string.main_point,
            description = fishingSpot.main_point,
        ),
    )
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

        NaverMap(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(300.dp)
                .clip(RoundedCornerShape(25.dp)),
            cameraPositionState = cameraPositionState,
        ) {
            Marker(
                state = MarkerState(position = fishingSpotPosition),
                height = 30.dp,
                width = 35.dp,
                icon = OverlayImage.fromResource(com.qure.core_design.R.drawable.bg_map_fill_marker),
            )
        }

        Row(modifier = Modifier.padding(top = 30.dp)) {
            Text(
                text = fishingSpot.fishing_spot_name,
                style = MaterialTheme.typography.displayLarge,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = fishingSpot.fishing_ground_type,
                style = MaterialTheme.typography.displayMedium,
                color = Gray500,
                fontSize = 18.sp,
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        fishingSpotData.forEach { (iconRes, type, description) ->
            val color =
                if (type == R.string.phone_number) Blue600 else MaterialTheme.colorScheme.onBackground
            val style = if (type == R.string.phone_number) {
                TextStyle(
                    textDecoration = TextDecoration.Underline,
                    color = color,
                )
            } else {
                MaterialTheme.typography.displaySmall
            }
            FishingSpotDataItem(
                iconRes = iconRes,
                type = type,
                description = description,
                descriptionFontStyle = style,
                onClickPhoneNumber = getOnClickPhoneNumber(type, onClickPhoneNumber),
            )
        }
    }
}

private fun getOnClickPhoneNumber(type: Int, onClickPhoneNumber: () -> Unit): () -> Unit {
    return if (type == R.string.phone_number) {
        onClickPhoneNumber
    } else {
        {}
    }
}

@Composable
private fun FishingSpotDataItem(
    @DrawableRes iconRes: Int?,
    @StringRes type: Int,
    description: String,
    descriptionFontStyle: TextStyle,
    onClickPhoneNumber: () -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        val guideLine = maxWidth * 0.3f
        val textOffsetX = maxWidth * 0.08f

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
        ) {
            if (iconRes != null) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(iconRes),
                    contentDescription = null,
                )
            } else {
                Spacer(modifier = Modifier.width(18.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .offset(x = textOffsetX),
                text = stringResource(type),
                style = MaterialTheme.typography.displaySmall,
                color = Gray300,
                fontSize = 15.sp,
            )
            Text(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 5.dp)
                    .offset(x = guideLine)
                    .clickable { onClickPhoneNumber() },
                text = description,
                style = descriptionFontStyle,
                fontSize = 15.sp,
            )
        }
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
