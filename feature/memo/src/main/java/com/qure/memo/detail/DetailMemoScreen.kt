package com.qure.memo.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qure.designsystem.component.DropDownItem
import com.qure.designsystem.component.FMDeleteDialog
import com.qure.designsystem.component.FMDropdownMenu
import com.qure.designsystem.component.FMMoreButton
import com.qure.designsystem.component.FMProgressBar
import com.qure.designsystem.component.FMShareDialog
import com.qure.designsystem.component.FMTopAppBar
import com.qure.designsystem.theme.GrayBackground
import com.qure.designsystem.utils.FMPreview
import com.qure.feature.memo.R
import com.qure.memo.share.DeepLinkHelper
import com.qure.memo.share.KakaoLinkSender
import com.qure.ui.component.PolaroidFrame
import com.qure.ui.model.MemoUI
import com.qure.ui.model.SnackBarMessageType
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailMemoRoute(
    memo: MemoUI,
    onBack: () -> Unit,
    onClickEdit: (MemoUI) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onShowMessageSnackBar: (messageType: SnackBarMessageType) -> Unit,
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
            .background(color = GrayBackground),
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
        showSnackBar = onShowMessageSnackBar,
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
    showSnackBar: (SnackBarMessageType) -> Unit = { },
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
                showSnackBar(SnackBarMessageType.DELETE_MEMO)
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

        Text(
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(R.string.polaroidDescription),
            style = MaterialTheme.typography.displayLarge,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
        )

        PolaroidFrame(
            modifier = Modifier
                .fillMaxWidth()
                .height(550.dp)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            fishType = memo.fishType,
            title = memo.title,
            imageUrl = memo.image,
            location = memo.location,
            date = memo.date,
            waterType = memo.waterType,
            fishSize = memo.fishSize,
            content = memo.content,
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
