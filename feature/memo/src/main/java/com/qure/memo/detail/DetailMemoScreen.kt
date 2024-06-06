package com.qure.memo.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qure.designsystem.component.DropDownItem
import com.qure.designsystem.component.FMDeleteDialog
import com.qure.designsystem.component.FMDropdownMenu
import com.qure.designsystem.component.FMGlideImage
import com.qure.designsystem.component.FMMoreButton
import com.qure.designsystem.component.FMProgressBar
import com.qure.designsystem.component.FMShareDialog
import com.qure.designsystem.component.FMTopAppBar
import com.qure.designsystem.theme.Gray700
import com.qure.designsystem.theme.White
import com.qure.designsystem.utils.FMPreview
import com.qure.feature.memo.R
import com.qure.memo.share.DeepLinkHelper
import com.qure.memo.share.KakaoLinkSender
import com.qure.ui.model.MemoUI
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailMemoRoute(
    memo: MemoUI,
    onBack: () -> Unit,
    onClickEdit: (MemoUI) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    viewModel: DetailMemoViewModel = hiltViewModel(),
) {
    val detailMemoUiState by viewModel.detailMemoUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel.error) {
        viewModel.error.collectLatest(onShowErrorSnackBar)
    }

    BackHandler(onBack = onBack)

    DetailMemoScreen(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colorScheme.background),
        memo = memo,
        detailMemoUiState = detailMemoUiState,
        onBack = onBack,
        onClickEdit = { onClickEdit(memo) },
        onClickDelete = { viewModel.deleteMemo(memo.uuid) },
        onClickKakaoShare = {
            KakaoLinkSender(
                context,
                memo
            ) { errorMessage ->
                onShowErrorSnackBar(Throwable(message = errorMessage))
            }.send()
        },
        onClickMoreShare = {
            DeepLinkHelper(context) { error ->
                viewModel.sendErrorMessage(Throwable(error))
            }.createDynamicLink(memo)
        },
    )
}

@Composable
private fun DetailMemoScreen(
    modifier: Modifier = Modifier,
    memo: MemoUI = MemoUI(),
    detailMemoUiState: DetailMemoUiState = DetailMemoUiState.Loading,
    onBack: () -> Unit = { },
    onClickDelete: () -> Unit = { },
    onClickEdit: () -> Unit = { },
    onClickKakaoShare: () -> Unit = { },
    onClickMoreShare: () -> Unit = { },
    showSnackBar: (String) -> Unit = { },
) {
    var isExpanded by remember { mutableStateOf(false) }
    var deleteDialogState by remember { mutableStateOf(false) }
    var shareDialogState by remember { mutableStateOf(false) }

    if (deleteDialogState) {
        FMDeleteDialog(
            title = stringResource(id = R.string.delete_memo_title),
            description = stringResource(id = R.string.delete_memo_description),
            onDismiss = { deleteDialogState = false },
            onClickDelete = onClickDelete,
            cancel = stringResource(id = R.string.cancel),
            delete = stringResource(id = R.string.delete),
        )
    }

    if (shareDialogState) {
        FMShareDialog(
            title = stringResource(id = R.string.share_memo),
            kakaoTalkShare = stringResource(id = R.string.kakao_talk),
            moreShare = stringResource(id = R.string.more),
            onClickKakao = { onClickKakaoShare() },
            onClickMore = { onClickMoreShare() },
            onDismiss = { shareDialogState = false },
        )
    }
    Column(
        modifier = modifier,
    ) {
        when (detailMemoUiState) {
            DetailMemoUiState.Initial -> Unit
            DetailMemoUiState.Loading -> {
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

            DetailMemoUiState.Success -> {
                showSnackBar(stringResource(id = R.string.delete_success_message))
                onBack()
            }
        }
        FMTopAppBar(
            title = memo.title,
            titleColor = MaterialTheme.colorScheme.onBackground,
            navigationIconColor = MaterialTheme.colorScheme.onBackground,
            onBack = onBack,
            titleFontSize = 15.sp,
        ) {
            FMMoreButton(
                modifier = Modifier.size(25.dp),
                onClickMore = { isExpanded = !isExpanded },
            )
            FMDropdownMenu(
                modifier = Modifier
                    .width(200.dp)
                    .background(color = MaterialTheme.colorScheme.background),
                showMenu = isExpanded,
                onDismiss = { isExpanded = false },
                menuItems = listOf(
                    DropDownItem(
                        menuName = stringResource(id = R.string.share),
                        onClick = { shareDialogState = true },
                    ),
                    DropDownItem(
                        menuName = stringResource(id = R.string.edit),
                        onClick = onClickEdit,
                    ),
                    DropDownItem(
                        menuName = stringResource(id = R.string.delete),
                        onClick = { deleteDialogState = true },
                    ),
                ),
            )
        }

        FMGlideImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
            model = memo.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = Gray700,
                        shape = RoundedCornerShape(15.dp),
                    ),
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    text = memo.waterType,
                    style = MaterialTheme.typography.displayLarge,
                    color = White,
                    fontSize = 15.sp,
                )
            }

            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = memo.fishType,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = stringResource(id = R.string.fish_length, memo.fishSize),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier
                    .padding(start = 20.dp),
                text = memo.date,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        Text(
            modifier = Modifier.padding(top = 20.dp, start = 20.dp),
            text = memo.title,
            style = MaterialTheme.typography.displayMedium,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Text(
            modifier = Modifier.padding(top = 10.dp, start = 20.dp),
            text = memo.location,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 20.dp, bottom = 30.dp),
            text = memo.content,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun DetailMemoScreenPreview() = FMPreview(false) {
    DetailMemoScreen(
        memo = MemoUI(
            title = "test",
            content = "test",
            fishSize = "12",
            fishType = "민어",
            waterType = "바다",
            date = "2024/01/01",
        ),
    )
}
