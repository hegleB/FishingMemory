package com.qure.memo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.qure.core.util.FishingMemoryToast
import com.qure.core_design.compose.components.FMBackButton
import com.qure.core_design.compose.components.FMCircleAddButton
import com.qure.core_design.compose.components.FMProgressBar
import com.qure.core_design.compose.components.FMRefreshLayout
import com.qure.core_design.compose.theme.Gray200
import com.qure.core_design.compose.theme.Gray700
import com.qure.core_design.compose.utils.FMPreview
import com.qure.memo.model.MemoUI
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MemoListScreen(
    viewModel: MemoListViewModel,
    onBack: () -> Unit,
    navigateToMemoCreate: () -> Unit,
    navigateToMemoDetail: (MemoUI) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val error = viewModel.error
    val memoListState = rememberLazyListState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(error) {
        error.collectLatest { message ->
            FishingMemoryToast().error(
                context,
                message,
            )
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.getFilteredMemo()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    MemoListContent(
        memos = uiState.filteredMemo,
        modifier = Modifier.fillMaxSize(),
        memoListState = memoListState,
        onRefresh = { viewModel.getFilteredMemo() },
        onBack = onBack,
        navigateToMemoCreate = navigateToMemoCreate,
        navigateToMemoDetail = navigateToMemoDetail,
        isFilterInitialized = uiState.isFilterInitialized,
        isLoading = viewModel.isLoading.value,
    )
}

@Composable
private fun MemoListContent(
    memos: List<MemoUI>,
    modifier: Modifier = Modifier,
    memoListState: LazyListState,
    onRefresh: () -> Unit,
    onBack: () -> Unit,
    navigateToMemoCreate: () -> Unit,
    navigateToMemoDetail: (MemoUI) -> Unit,
    isFilterInitialized: Boolean,
    isLoading: Boolean,
) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        Box(
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp, top = 30.dp)
                .fillMaxWidth(),
        ) {
            FMBackButton(
                modifier = Modifier.size(25.dp),
                onClickBack = { onBack() },
                backIcon = painterResource(id = com.qure.core_design.R.drawable.ic_arrow_back),
                iconColor = MaterialTheme.colorScheme.onBackground,
            )
            FMCircleAddButton(
                modifier = Modifier
                    .size(25.dp)
                    .align(Alignment.CenterEnd),
                onClickAdd = { navigateToMemoCreate() },
                circleAddIcon = painterResource(id = com.qure.core_design.R.drawable.ic_circle_add),
                iconColor = MaterialTheme.colorScheme.onBackground,
            )
        }
        FMRefreshLayout(
            onRefresh = { onRefresh() },
        ) {
            if (isLoading) {
                val scrollState = rememberScrollState()
                Row(
                    modifier = modifier
                        .verticalScroll(scrollState)
                        .background(color = MaterialTheme.colorScheme.background),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FMProgressBar(
                        modifier = Modifier
                            .size(50.dp),
                    )
                }
            } else {
                if (isFilterInitialized) {
                    if (memos.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = MaterialTheme.colorScheme.background),
                            state = memoListState,
                        ) {
                            items(memos) { memo ->
                                MemoItem(
                                    memo = memo,
                                    navigateToMemoDetail = navigateToMemoDetail,
                                )
                            }
                        }
                    } else {
                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.fishing))
                        val progress by animateLottieCompositionAsState(
                            composition = composition,
                            isPlaying = true,
                            restartOnPlay = true,
                            iterations = Int.MAX_VALUE,
                        )
                        val scrollState = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                                .background(color = MaterialTheme.colorScheme.background),
                        ) {
                            LottieAnimation(
                                composition = composition,
                                progress = { progress },
                                modifier = Modifier
                                    .height(350.dp)
                                    .fillMaxWidth()
                                    .background(color = MaterialTheme.colorScheme.background),
                            )

                            Text(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                text = stringResource(id = R.string.empty_memo_title),
                                fontSize = 18.sp,
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                            Text(
                                modifier = Modifier
                                    .padding(top = 5.dp)
                                    .align(Alignment.CenterHorizontally),
                                text = stringResource(id = R.string.empty_memo_description),
                                fontSize = 12.sp,
                                style = MaterialTheme.typography.displayMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun MemoItem(
    memo: MemoUI,
    navigateToMemoDetail: (MemoUI) -> Unit,
) {
    Column(modifier = Modifier.clickable { navigateToMemoDetail(memo) }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(top = 10.dp, start = 10.dp),
        ) {
            GlideImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(150.dp)
                    .padding(bottom = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(
                        width = 2.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(10.dp),
                    ),
                model = memo.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier
                    .weight(0.75f)
                    .padding(start = 20.dp),
            ) {
                Text(
                    text = memo.title,
                    style = MaterialTheme.typography.displayMedium,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 15.sp,
                )
                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = memo.location,
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.displaySmall,
                )
                Text(
                    modifier = Modifier
                        .padding(top = 10.dp),
                    text = memo.content,
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.3f),
            ) {
                Column(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(
                            color = Gray700,
                        ),
                ) {
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        text = memo.fishType,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        maxLines = 1,
                    )
                    Spacer(modifier = Modifier.height(1.dp))
                }

                Text(
                    modifier = Modifier
                        .padding(end = 10.dp, bottom = 5.dp)
                        .align(Alignment.BottomCenter),
                    text = memo.date,
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
            thickness = 1.dp,
            color = Gray200,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun MemoListScreenPreview() = FMPreview {
    MemoListContent(
        memos = listOf(
            MemoUI(
                image = "https://www.pexels.com/photo/close-up-photo-of-clownfish-128756/",
                name = "미꾸라지",
                location = "서울특별시 강남구",
                title = "제목1",
                content = "내용내용내용내용 내용",
                date = "2024/02/05",
            ),
            MemoUI(
                image = "https://unsplash.com/ko/%EC%82%AC%EC%A7%84/%ED%9A%8C%EC%83%89%EA%B3%BC-%EB%85%B8%EB%9E%80%EC%83%89-%EB%AC%BC%EA%B3%A0%EA%B8%B0-iLwQIbWxv-s",
                name = "붕어",
                location = "서울특별시 서초구",
                title = "제목2",
                content = "내용내용내용내용 내용12내용내용내치hjgjhg",
                date = "2024/02/05",
            ),
        ),
        memoListState = LazyListState(),
        onRefresh = { },
        onBack = { },
        navigateToMemoCreate = { },
        navigateToMemoDetail = { },
        isLoading = false,
        isFilterInitialized = false,
    )
}
