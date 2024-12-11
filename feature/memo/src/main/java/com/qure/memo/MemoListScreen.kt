package com.qure.memo

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qure.designsystem.component.FMBackButton
import com.qure.designsystem.component.FMCircleAddButton
import com.qure.designsystem.component.FMGlideImage
import com.qure.designsystem.component.FMLottieAnimation
import com.qure.designsystem.component.FMProgressBar
import com.qure.designsystem.component.FMRefreshLayout
import com.qure.designsystem.theme.Gray200
import com.qure.designsystem.theme.Gray700
import com.qure.designsystem.utils.FMPreview
import com.qure.feature.memo.R
import com.qure.ui.model.MemoUI
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MemoListRoute(
    onBack: () -> Unit,
    navigateToMemoCreate: () -> Unit,
    navigateToMemoDetail: (MemoUI) -> Unit,
    viewModel: MemoListViewModel = hiltViewModel(),
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,

    ) {
    val memoListUiState by viewModel.memoListUiState.collectAsStateWithLifecycle()
    var isRefresh by remember { mutableStateOf(false) }

    BackHandler(onBack = onBack)

    LaunchedEffect(viewModel.error) {
        viewModel.error.collectLatest(onShowErrorSnackBar)
    }

    LaunchedEffect(Unit) {
        viewModel.fetchMemoList()
    }

    MemoListScreen(
        memoListUiState = memoListUiState,
        modifier = Modifier.fillMaxSize(),
        onRefresh = {
            viewModel.fetchMemoList()
            isRefresh = true
        },
        onBack = onBack,
        navigateToMemoCreate = navigateToMemoCreate,
        navigateToMemoDetail = navigateToMemoDetail,
        isRefresh = isRefresh,
    )
}

@Composable
private fun MemoListScreen(
    memoListUiState: MemoListUiState = MemoListUiState.Loading,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = { },
    onBack: () -> Unit = { },
    navigateToMemoCreate: () -> Unit = { },
    navigateToMemoDetail: (MemoUI) -> Unit = { },
    isRefresh: Boolean = false,
) {
    val memoListState = rememberLazyListState()
    val scrollState = rememberScrollState()
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
                iconColor = MaterialTheme.colorScheme.onBackground,
            )
            FMCircleAddButton(
                modifier = Modifier
                    .size(25.dp)
                    .align(Alignment.CenterEnd),
                onClickAdd = { navigateToMemoCreate() },
                iconColor = MaterialTheme.colorScheme.onBackground,
            )
        }
        FMRefreshLayout(
            onRefresh = { onRefresh() },
            isRefresh = if (isRefresh) memoListUiState is MemoListUiState.Loading else false,
        ) {
            when (memoListUiState) {
                MemoListUiState.Loading -> {
                    if (isRefresh.not()) {
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
                    }
                }

                is MemoListUiState.Success -> {
                    if (memoListUiState.memos.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
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
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = MaterialTheme.colorScheme.background),
                            state = memoListState,
                        ) {
                            items(memoListUiState.memos) { memo ->
                                MemoItem(
                                    memo = memo,
                                    navigateToMemoDetail = navigateToMemoDetail,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


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
            FMGlideImage(
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
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = memo.location,
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    modifier = Modifier
                        .padding(top = 10.dp),
                    text = memo.content,
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
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
                    color = MaterialTheme.colorScheme.onBackground,
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
        onRefresh = { },
        onBack = { },
        navigateToMemoCreate = { },
        navigateToMemoDetail = { },
    )
}
