package com.qure.memo

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qure.designsystem.component.FMBackButton
import com.qure.designsystem.component.FMCircleAddButton
import com.qure.designsystem.component.FMLottieAnimation
import com.qure.designsystem.component.FMProgressBar
import com.qure.designsystem.theme.GrayBackground
import com.qure.designsystem.utils.FMPreview
import com.qure.designsystem.utils.clickableWithoutRipple
import com.qure.feature.memo.R
import com.qure.ui.component.CardFront
import com.qure.ui.component.PolaroidLayout
import com.qure.ui.component.Tape
import com.qure.ui.model.MemoUI
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.absoluteValue

@Composable
fun MemoListRoute(
    onBack: () -> Unit,
    navigateToMemoCreate: () -> Unit,
    navigateToMemoDetail: (MemoUI) -> Unit,
    viewModel: MemoListViewModel = hiltViewModel(),
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    val memoListUiState by viewModel.memoListUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchMemoList()
    }

    LaunchedEffect(viewModel.error) {
        viewModel.error.collectLatest(onShowErrorSnackBar)
    }

    MemoListScreen(
        memoListUiState = memoListUiState,
        onBack = onBack,
        navigateToMemoCreate = navigateToMemoCreate,
        navigateToMemoDetail = navigateToMemoDetail,
    )
}

@Composable
private fun MemoListScreen(
    memoListUiState: MemoListUiState = MemoListUiState.Loading,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = { },
    navigateToMemoCreate: () -> Unit = { },
    navigateToMemoDetail: (MemoUI) -> Unit = { },
) {
    val scrollState = rememberSaveable(saver = ScrollState.Saver) { ScrollState(0) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(onBack, navigateToMemoCreate)
        }
    ) { paddingValue ->

        when (memoListUiState) {
            MemoListUiState.Loading -> MemoLoading()

            is MemoListUiState.Success -> {
                if (memoListUiState.memos.isEmpty()) {
                    MemoListEmpty(
                        paddingValue = paddingValue,
                        scrollState = scrollState,
                    )
                } else {
                    MemoList(
                        paddingValue = paddingValue,
                        scrollState = scrollState,
                        uiState = memoListUiState,
                        navigateToMemoDetail = navigateToMemoDetail,
                    )
                }
            }
        }
    }
}

@Composable
private fun MemoList(
    paddingValue: PaddingValues,
    scrollState: ScrollState,
    uiState: MemoListUiState.Success,
    navigateToMemoDetail: (MemoUI) -> Unit
) {
    var isVisibleList = rememberSaveable(
        saver = Saver<MutableList<Boolean>, MutableList<Boolean>>(
            save = { it },
            restore = { it }
        )
    ) {
        MutableList(uiState.memos.size) { false }
    }

    if (isVisibleList.size != uiState.memos.size) {
        if (isVisibleList.size < uiState.memos.size) {
            repeat(uiState.memos.size - isVisibleList.size) {
                isVisibleList.add(false)
            }
        } else {
            repeat(isVisibleList.size - uiState.memos.size) {
                isVisibleList.removeAt(isVisibleList.lastIndex)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValue)
            .background(color = MaterialTheme.colorScheme.surfaceTint)
            .verticalScroll(scrollState),
    ) {
        PolaroidLayout(
            modifier = Modifier.padding(top = 20.dp),
            itemCount = uiState.memos.size,
        ) { index ->
            val memo = uiState.memos[index]
            val isEven = index % 2 == 0

            var targetAlpha by rememberSaveable { mutableStateOf(0f) }
            var targetOffsetY by rememberSaveable { mutableStateOf(300f) }

            val animatedAlpha by animateFloatAsState(
                targetValue = targetAlpha,
                animationSpec = tween(durationMillis = 300)
            )
            val animatedOffsetY by animateFloatAsState(
                targetValue = targetOffsetY,
                animationSpec = tween(durationMillis = 300)
            )
            val isVisible = isVisibleList[index]

            LaunchedEffect(isVisible) {
                if (!isVisible) {
                    delay(index * 100L)
                    isVisibleList[index] = true
                    targetAlpha = 1f
                    targetOffsetY = 0f
                }
            }

            Box(
                modifier = Modifier
                    .size(280.dp)
                    .graphicsLayer {
                        rotationZ = if (isEven) -15f else 15f
                    }
                    .alpha(animatedAlpha)
                    .offset(
                        y = animatedOffsetY.dp,
                    ),
            ) {
                CardFront(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .background(color = Color.White)
                        .clickableWithoutRipple { navigateToMemoDetail(memo) },
                    fishType = memo.fishType,
                    waterType = memo.waterType,
                    size = memo.fishSize,
                    imageUrl = memo.image,
                )
                Tape(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .graphicsLayer {
                            rotationZ = if (isEven) 50f else -50f
                            rotationY = 180f
                        }
                        .height(65.dp)
                        .width(30.dp)
                        .alpha(0.5f)
                        .background(color = Color.Transparent)
                )
            }
        }
    }
}

@Composable
private fun MemoListEmpty(
    paddingValue: PaddingValues,
    scrollState: ScrollState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValue)
            .verticalScroll(scrollState)
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        FMLottieAnimation(
            modifier = Modifier
                .height(350.dp)
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background),
            lottieId = com.qure.core.designsystem.R.raw.fishing,
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

@Composable
private fun MemoLoading(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        FMProgressBar(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.Center),
        )
    }
}

@Composable
private fun TopAppBar(onBack: () -> Unit, navigateToMemoCreate: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(start = 30.dp, end = 30.dp)
            .fillMaxWidth()
            .height(50.dp),
    ) {
        FMBackButton(
            modifier = Modifier
                .size(25.dp)
                .align(Alignment.CenterStart),
            onClickBack = { onBack() },
            iconColor = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = stringResource(R.string.memo_list_app_bar_title),
            style = MaterialTheme.typography.displayLarge,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
        FMCircleAddButton(
            modifier = Modifier
                .size(25.dp)
                .align(Alignment.CenterEnd),
            onClickAdd = { navigateToMemoCreate() },
            iconColor = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun MemoListScreenPreview() = FMPreview {
    MemoListScreen(
        memoListUiState = MemoListUiState.Success(
            persistentListOf(
                MemoUI(
                    image = "https://www.pexels.com/photo/close-up-photo-of-clownfish-128756/",
                    location = "서울특별시 강남구",
                    title = "제목1",
                    content = "내용내용내용내용 내용",
                    date = "2024/02/05",
                ),
                MemoUI(
                    image = "https://unsplash.com/ko/%EC%82%AC%EC%A7%84/%ED%9A%8C%EC%83%89%EA%B3%BC-%EB%85%B8%EB%9E%80%EC%83%89-%EB%AC%BC%EA%B3%A0%EA%B8%B0-iLwQIbWxv-s",
                    location = "서울특별시 서초구",
                    title = "제목2",
                    content = "내용내용내용내용 내용12내용내용내치hjgjhg",
                    date = "2024/02/05",
                ),
            ),
        ),
        onBack = { },
        navigateToMemoCreate = { },
        navigateToMemoDetail = { },
    )
}
