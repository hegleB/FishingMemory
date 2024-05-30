package com.qure.fishingspot.bookmark

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qure.core_design.compose.components.FMDeleteDialog
import com.qure.core_design.compose.components.FMFishingSpotItem
import com.qure.core_design.compose.components.FMProgressBar
import com.qure.core_design.compose.components.FMTopAppBar
import com.qure.core_design.compose.theme.Gray300
import com.qure.core_design.compose.theme.Gray700
import com.qure.core_design.compose.theme.White
import com.qure.core_design.compose.utils.FMPreview
import com.qure.fishingspot.FishingSpotUiState
import com.qure.fishingspot.R
import com.qure.model.FishingSpotUI

@Composable
fun BookmarkRoute(
    viewModel: BookmarkViewModel,
    onBack: () -> Unit,
    navigateToFishingSpot: (FishingSpotUI) -> Unit,
    onClickPhoneNumber: (String) -> Unit,
) {
    val fishingSpotUiState by viewModel.fishingSpotUiState.collectAsStateWithLifecycle()
    val lifecycleObserver = LocalLifecycleOwner.current
    DisposableEffect(lifecycleObserver) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.fetchFishingSpotBookmark()
            }
        }
        lifecycleObserver.lifecycle.addObserver(observer)

        onDispose {
            lifecycleObserver.lifecycle.removeObserver(observer)
        }
    }

    BookmarkContent(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        onBack = onBack,
        fishingSpotUiState = fishingSpotUiState,
        navigateToFishingSpot = navigateToFishingSpot,
        onClickPhoneNumber = onClickPhoneNumber,
        onClickDelete = { viewModel.deleteAllBookmarks() },
    )
}

@Composable
private fun BookmarkContent(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = { },
    fishingSpotUiState: FishingSpotUiState,
    navigateToFishingSpot: (FishingSpotUI) -> Unit = { },
    onClickPhoneNumber: (String) -> Unit = { },
    onClickDelete: () -> Unit = { },
) {
    var dialogState by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (dialogState) {
            FMDeleteDialog(
                title = stringResource(id = R.string.message_deleteAll_bookmark),
                description = stringResource(id = R.string.message_deleteAll_description),
                onDismiss = { dialogState = false },
                cancel = stringResource(id = com.qure.memo.R.string.cancel),
                delete = stringResource(id = com.qure.memo.R.string.delete),
                onClickDelete = { onClickDelete() },
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
                            .clickable { dialogState = true },
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.displaySmall,
                    )
                }
            }

            when (fishingSpotUiState) {
                FishingSpotUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                    ) {
                        FMProgressBar(
                            modifier = Modifier
                                .align(Alignment.Center),
                        )
                    }
                }

                is FishingSpotUiState.Success -> {
                    if (fishingSpotUiState.bookmarks.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                        ) {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = stringResource(id = R.string.message_empty_bookmark),
                                fontSize = 15.sp,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Gray300,
                            )
                        }
                    } else {
                        LazyColumn {
                            items(fishingSpotUiState.bookmarks) { bookmark ->
                                FMFishingSpotItem(
                                    fishingSpotName = bookmark.fishing_spot_name,
                                    fishingGroundType = bookmark.fishing_ground_type,
                                    numberAddress = bookmark.number_address,
                                    roadAddress = bookmark.road_address,
                                    phoneNumber = bookmark.phone_number,
                                    fishType = bookmark.fish_type,
                                    mainPoint = bookmark.main_point,
                                    fee = bookmark.fee,
                                    onFishingSpotClicked = { navigateToFishingSpot(bookmark) },
                                    onClickPhoneNumber = onClickPhoneNumber,
                                )
                                Divider()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun BookmarkScreenPreview() = FMPreview {
    BookmarkContent(
        fishingSpotUiState = FishingSpotUiState.Loading,
    )
}
