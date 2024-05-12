package com.qure.memo.detail

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.qure.core.util.FishingMemoryToast
import com.qure.core_design.compose.components.DropDownItem
import com.qure.core_design.compose.components.FMDeleteDialog
import com.qure.core_design.compose.components.FMDropdownMenu
import com.qure.core_design.compose.components.FMMoreButton
import com.qure.core_design.compose.components.FMShareDialog
import com.qure.core_design.compose.components.FMTopAppBar
import com.qure.core_design.compose.theme.Gray700
import com.qure.core_design.compose.theme.White
import com.qure.core_design.compose.utils.FMPreview
import com.qure.memo.R
import com.qure.memo.model.MemoUI
import com.qure.memo.share.DeepLinkHelper
import com.qure.memo.share.KakaoLinkSender

@Composable
fun DetailMemoScreen(
    memo: MemoUI,
    viewModel: DetailMemoViewModel,
    onBack: () -> Unit,
    onClickEdit: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(uiState) {
        if (uiState.isDeleteInitialized) {
            FishingMemoryToast().show(context, uiState.deleteSuccessMessage)
            onBack()
        }
    }
    DetailMemoContent(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colorScheme.background),
        memo = memo,
        onBack = onBack,
        onClickEdit = onClickEdit,
        onClickDelete = { viewModel.deleteMemo(memo.uuid) },
        onClickKakaoShare = { KakaoLinkSender(context, memo).send() },
        onClickMoreShare = { DeepLinkHelper(context).createDynamicLink(memo) },
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun DetailMemoContent(
    modifier: Modifier = Modifier,
    memo: MemoUI = MemoUI(),
    onBack: () -> Unit = { },
    onClickDelete: () -> Unit = { },
    onClickEdit: () -> Unit = { },
    onClickKakaoShare: () -> Unit = { },
    onClickMoreShare: () -> Unit = { },
) {
    var isExpanded = remember { mutableStateOf(false) }
    var deleteDialogState = remember { mutableStateOf(false) }
    var shareDialogState = remember { mutableStateOf(false) }

    if (deleteDialogState.value) {
        FMDeleteDialog(
            title = stringResource(id = R.string.delete_memo_title),
            description = stringResource(id = R.string.delete_memo_description),
            onDismiss = { deleteDialogState.value = false },
            onClickDelete = onClickDelete,
            cancel = stringResource(id = R.string.cancel),
            delete = stringResource(id = R.string.delete),
        )
    }

    if (shareDialogState.value) {
        FMShareDialog(
            title = stringResource(id = R.string.share_memo),
            kakaoTalkShare = stringResource(id = R.string.kakao_talk),
            moreShare = stringResource(id = R.string.more),
            onClickKakao = { onClickKakaoShare() },
            onClickMore = { onClickMoreShare() },
            onDismiss = { shareDialogState.value = false },
        )
    }
    Column(
        modifier = modifier,
    ) {
        FMTopAppBar(
            title = memo.fishType,
            titleColor = MaterialTheme.colorScheme.onBackground,
            navigationIconColor = MaterialTheme.colorScheme.onBackground,
            onBack = onBack,
            titleFontSize = 15.sp,
        ) {
            FMMoreButton(
                modifier = Modifier.size(25.dp),
                onClickMore = { isExpanded.value = !isExpanded.value },
            )
            FMDropdownMenu(
                modifier = Modifier
                    .width(200.dp)
                    .background(color = MaterialTheme.colorScheme.background),
                showMenu = isExpanded.value,
                onDismiss = { isExpanded.value = false },
                menuItems = listOf(
                    DropDownItem(
                        menuName = stringResource(id = R.string.share),
                        onClick = { shareDialogState.value = true },
                    ),
                    DropDownItem(
                        menuName = stringResource(id = R.string.edit),
                        onClick = onClickEdit,
                    ),
                    DropDownItem(
                        menuName = stringResource(id = R.string.delete),
                        onClick = { deleteDialogState.value = true },
                    ),
                ),
            )
        }

        GlideImage(
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
            )

            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = stringResource(id = R.string.fish_length, memo.fishSize),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 15.sp,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier
                    .padding(start = 20.dp),
                text = memo.date,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 15.sp,
            )
        }

        Text(
            modifier = Modifier.padding(top = 20.dp, start = 20.dp),
            text = memo.title,
            style = MaterialTheme.typography.displayMedium,
            fontSize = 15.sp,
        )

        Text(
            modifier = Modifier.padding(top = 10.dp, start = 20.dp),
            text = memo.location,
            fontSize = 12.sp,
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 20.dp, bottom = 30.dp),
            text = memo.content,
            fontSize = 12.sp,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun DetailMemoScreenPreview() = FMPreview(false) {
    DetailMemoContent(
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