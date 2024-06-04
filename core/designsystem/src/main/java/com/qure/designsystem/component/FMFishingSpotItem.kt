package com.qure.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.qure.core.designsystem.R
import com.qure.model.fishingspot.FishingSpotData
import com.qure.designsystem.utils.clickableWithoutRipple
import com.qure.designsystem.theme.Blue600
import com.qure.designsystem.theme.Gray300
import com.qure.designsystem.theme.Gray500
import com.qure.designsystem.theme.White

@Composable
fun FMFishingSpotItem(
    modifier: Modifier = Modifier,
    fishingSpotName: String = "",
    fishingGroundType: String = "",
    numberAddress: String = "",
    roadAddress: String = "",
    phoneNumber: String = "",
    fishType: String = "",
    mainPoint: String = "",
    fee: String = "",
    onClickPhoneNumber: (String) -> Unit = { },
    onFishingSpotClicked: () -> Unit = { },
) {
    val fishingSpotData = listOf(
        FishingSpotData(
            iconRes = R.drawable.ic_empty_marker,
            type = stringResource(id = R.string.number_address),
            description = numberAddress,
        ),
        FishingSpotData(
            iconRes = null,
            type = stringResource(id = R.string.road_address),
            description = roadAddress
        ),
        FishingSpotData(
            iconRes = R.drawable.ic_phone_number,
            type = stringResource(id = R.string.phone_number),
            description = phoneNumber,
        ),
        FishingSpotData(
            iconRes = R.drawable.ic_fish,
            type = stringResource(id = R.string.fish_type),
            description = fishType,
        ),
        FishingSpotData(
            iconRes = R.drawable.ic_won,
            type = stringResource(id = R.string.fee),
            description = fee
        ),
        FishingSpotData(
            iconRes = R.drawable.ic_fishing,
            type = stringResource(id = R.string.main_point),
            description = mainPoint,
        ),
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp)
            .padding(horizontal = 20.dp)
            .clickableWithoutRipple { onFishingSpotClicked() },
    ) {
        Row {
            Text(
                text = fishingSpotName,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = fishingGroundType,
                style = MaterialTheme.typography.displayMedium,
                color = Gray500,
            )
        }
        fishingSpotData.forEach { info ->
            FishingSpotDataItem(
                icon = info.iconRes,
                type = info.type,
                description = info.description,
                onClickPhoneNumber = onClickPhoneNumber,
            )
        }
    }
}

@Composable
private fun FishingSpotDataItem(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int?,
    type: String,
    description: String,
    onClickPhoneNumber: (String) -> Unit = { },
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 3.dp, bottom = 5.dp),
        verticalAlignment = Alignment.Top,
    ) {
        icon?.let { iconId ->
            Icon(
                modifier = Modifier
                    .size(18.dp),
                painter = painterResource(id = iconId),
                contentDescription = null,
                tint = if (isSystemInDarkTheme()) White else Gray300,
            )
        } ?: run {
            Spacer(modifier = Modifier.width(18.dp))
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            modifier = Modifier
                .width(100.dp),
            text = type,
            maxLines = 2,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Text(
            modifier = if (type == stringResource(id = R.string.phone_number)) {
                Modifier.clickable { onClickPhoneNumber(description) }
            } else {
                Modifier
            },
            text = description,
            color = if (type == stringResource(id = R.string.phone_number)) {
                Blue600
            } else {
                MaterialTheme.colorScheme.onBackground
            },
            maxLines = 2,
            style = if (type == stringResource(id = R.string.phone_number)) {
                TextStyle(
                    textDecoration = TextDecoration.Underline,
                )
            } else {
                MaterialTheme.typography.displaySmall
            }
        )
    }
}