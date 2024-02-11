package com.qure.fishingspot.bookmark

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qure.core.extensions.Empty
import com.qure.core_design.compose.components.FMBookmarkDeleteDialog
import com.qure.core_design.compose.components.FMProgressBar
import com.qure.core_design.compose.components.FMTopAppBar
import com.qure.core_design.compose.theme.Blue600
import com.qure.core_design.compose.theme.Gray300
import com.qure.core_design.compose.theme.Gray500
import com.qure.core_design.compose.theme.Gray700
import com.qure.core_design.compose.theme.White
import com.qure.core_design.compose.utils.FMPreview
import com.qure.fishingspot.FishingSpotData
import com.qure.fishingspot.R
import com.qure.model.FishingSpotUI

@Composable
fun BookmarkScreen(
    viewModel: BookmarkViewModel,
    onBack: () -> Unit,
    navigateToFishingSpot: (FishingSpotUI) -> Unit,
    onClickPhoneNumber: (String) -> Unit,
) {
    val bookmarks by viewModel.fishingSpotBookmarks.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    BookmarkContent(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        onBack = onBack,
        bookmarks = bookmarks,
        isLoading = isLoading,
        navigateToFishingSpot = navigateToFishingSpot,
        onClickPhoneNumber = onClickPhoneNumber,
        onClickDelete = { viewModel.deleteAllBookmarks() },
    )
}

@Composable
private fun BookmarkContent(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = { },
    bookmarks: List<FishingSpotUI> = emptyList(),
    isLoading: Boolean = false,
    navigateToFishingSpot: (FishingSpotUI) -> Unit = { },
    onClickPhoneNumber: (String) -> Unit = { },
    onClickDelete: () -> Unit = { },
) {
    var dialogState = remember {
        mutableStateOf(false)
    }
    Box(modifier = modifier) {
        if (dialogState.value) {
            FMBookmarkDeleteDialog(
                title = stringResource(id = R.string.message_deleteAll_bookmark),
                description = stringResource(id = R.string.message_deleteAll_description),
                onDismiss = { dialogState.value = false },
                cancel = stringResource(id = com.qure.memo.R.string.cancel),
                delete = stringResource(id = com.qure.memo.R.string.delete),
                onClickDelete = { onClickDelete() }
            )
        }
        if (isLoading) {
            FMProgressBar(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.Center),
            )
        } else {
            if (bookmarks.isEmpty()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = R.string.message_empty_bookmark),
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray300,
                )
            }
            Column {
                FMTopAppBar(
                    modifier = Modifier.padding(end = 30.dp),
                    title = stringResource(id = R.string.fishingspot_bookmark),
                    titleColor = MaterialTheme.colorScheme.onBackground,
                    onBack = { onBack() },
                    navigationIconColor = MaterialTheme.colorScheme.onBackground,
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Gray700,
                                shape = RoundedCornerShape(15.dp),
                            ),
                    ) {
                        Text(
                            text = stringResource(id = R.string.all_delete),
                            color = White,
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .clickable { dialogState.value = true },
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.displaySmall,
                        )
                    }
                }
                if (bookmarks.isNotEmpty()) {
                    LazyColumn {
                        items(bookmarks) { bookmark ->
                            BookmarkItem(
                                fishingSpot = bookmark,
                                navigateToFishingSpot = navigateToFishingSpot,
                                onClickPhoneNumber = onClickPhoneNumber,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BookmarkItem(
    fishingSpot: FishingSpotUI,
    navigateToFishingSpot: (FishingSpotUI) -> Unit,
    onClickPhoneNumber: (String) -> Unit,
) {
    val fishingSpotData = listOf(
        FishingSpotData(
            com.qure.core_design.R.drawable.ic_empty_marker,
            R.string.number_address,
            fishingSpot.number_address,
        ),
        FishingSpotData(null, R.string.road_address, fishingSpot.road_address),
        FishingSpotData(
            com.qure.core_design.R.drawable.ic_phone_number,
            R.string.phone_number,
            fishingSpot.phone_number,
        ),
        FishingSpotData(
            com.qure.core_design.R.drawable.ic_fish,
            R.string.fish_type,
            fishingSpot.fish_type,
        ),
        FishingSpotData(com.qure.core_design.R.drawable.ic_won, R.string.fee, fishingSpot.fee),
        FishingSpotData(
            com.qure.core_design.R.drawable.ic_fishing,
            R.string.main_point,
            fishingSpot.main_point,
        ),
    )

    Column(
        modifier = Modifier
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
            .clickable { navigateToFishingSpot(fishingSpot) },
    ) {
        FishingSpotNameAndType(fishingSpot)
        Spacer(modifier = Modifier.height(10.dp))
        fishingSpotData.forEach { dataItem ->
            val color =
                if (dataItem.type == R.string.phone_number) Blue600 else MaterialTheme.colorScheme.onBackground
            val style = if (dataItem.type == R.string.phone_number) {
                TextStyle(
                    textDecoration = TextDecoration.Underline,
                    color = color,
                )
            } else {
                MaterialTheme.typography.displaySmall
            }
            FishingSpotDataItem(
                dataItem,
                style,
                color,
                if (dataItem.type == R.string.phone_number) {
                    onClickPhoneNumber
                } else {
                    {}
                },
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
    Divider()
}

@Composable
private fun FishingSpotNameAndType(fishingSpot: FishingSpotUI) {
    Row {
        Text(
            text = fishingSpot.fishing_spot_name,
            style = MaterialTheme.typography.displayLarge,
            fontSize = 18.sp,
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = fishingSpot.fishing_ground_type,
            style = MaterialTheme.typography.displayMedium,
            color = Gray500,
            fontSize = 16.sp,
        )
    }
}

@Composable
private fun FishingSpotDataItem(
    dataItem: FishingSpotData,
    descriptionFontStyle: TextStyle,
    descriptionColor: Color,
    onClickPhoneNumber: (String) -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth(),
    ) {
        val guideLine = maxWidth * 0.25f
        val textOffsetX = maxWidth * 0.06f
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
        ) {
            IconOrSpacer(dataItem.iconRes)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(dataItem.type),
                modifier = Modifier.offset(x = textOffsetX),
                style = MaterialTheme.typography.displaySmall,
                color = Gray300,
                fontSize = 12.sp,
            )
            Text(
                text = dataItem.description,
                modifier = if (dataItem.type == R.string.phone_number) {
                    Modifier
                        .offset(x = guideLine)
                        .clickable {
                            onClickPhoneNumber(dataItem.description)
                        }
                } else {
                    Modifier.offset(x = guideLine)
                },
                style = descriptionFontStyle,
                fontSize = 12.sp,
                color = descriptionColor,
            )
        }
    }
}

@Composable
private fun IconOrSpacer(iconResId: Int?) {
    if (iconResId != null) {
        Icon(
            painter = painterResource(iconResId),
            contentDescription = null,
            modifier = Modifier.size(14.dp),
        )
    } else {
        Spacer(modifier = Modifier.width(14.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun BookmarkScreenPreview() = FMPreview {
    BookmarkContent(
        bookmarks = listOf(
            FishingSpotUI(
                road_address = "서울특별시 강남구",
                fishing_ground_type = "기타",
                number_address = "서울특별시 강남구 테헤란로",
                data_base_date = String.Empty,
                fish_type = "바다",
                main_point = "메인포인트",
                fishing_spot_name = "강남낚시",
                fee = "1000원",
                phone_number = "010-0000-0000",
            ),
        ),
    )
}
