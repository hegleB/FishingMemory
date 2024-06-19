package com.qure.create

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qure.designsystem.component.FMButton
import com.qure.designsystem.component.FMChipGroup
import com.qure.designsystem.component.FMGlideImage
import com.qure.designsystem.component.FMProgressBar
import com.qure.designsystem.component.FMTopAppBar
import com.qure.designsystem.theme.Blue500
import com.qure.designsystem.theme.Gray100
import com.qure.designsystem.theme.White
import com.qure.designsystem.utils.FMPreview
import com.qure.feature.create.R
import com.qure.ui.component.FMCalendarDialog
import com.qure.ui.component.OfflineView
import com.qure.ui.model.MemoUI
import com.qure.ui.model.SnackBarMessageType
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun MemoCreateRoute(
    memo: MemoUI,
    isEdit: Boolean = false,
    onBack: () -> Unit,
    navigateToLocationSetting: (MemoUI) -> Unit,
    navigateToGallery: (MemoUI) -> Unit,
    navigateToMemoDetail: (MemoUI) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onShowMessageSnackBar: (messageType: SnackBarMessageType) -> Unit,
    viewModel: MemoViewModel = hiltViewModel(),
) {

    val editMode by viewModel.editMode.collectAsStateWithLifecycle()
    LaunchedEffect(isEdit) {
        if (isEdit && editMode.not()) {
            viewModel.setMemoUi(memo)
            viewModel.setEditMode(true)
        }
    }

    LaunchedEffect(viewModel.message) {
        viewModel.message.collectLatest(onShowMessageSnackBar)
    }

    LaunchedEffect(viewModel.error) {
        viewModel.error.collectLatest(onShowErrorSnackBar)
    }

    val uiState by viewModel.memoCreateUiState.collectAsStateWithLifecycle()
    val memo by viewModel.memo.collectAsStateWithLifecycle()
    val isConnectNetwork by viewModel.isConnectNetwork.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )
    val resources = LocalContext.current.resources
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        if (areGranted.not()) {
            viewModel.sendErrorMessage(Throwable(resources.getString(androidx.compose.ui.R.string.default_error_message)))
        }
    }
    LaunchedEffect(requestPermissionLauncher) {
        checkAndRequestPermissions(context, permissions, requestPermissionLauncher)
    }

    BackHandler(onBack = onBack)

    MemoCreateScreen(
        uiState = uiState,
        onBack = onBack,
        memo = memo,
        navigateToLocationSetting = { navigateToLocationSetting(memo) },
        navigateToGallery = { navigateToGallery(memo) },
        navigateToMemoDetail = navigateToMemoDetail,
        setDate = viewModel::setDate,
        setTitle = viewModel::setTitle,
        setSize = viewModel::setSize,
        setContent = viewModel::setContent,
        setFishType = viewModel::setFishType,
        setWaterType = viewModel::setWaterType,
        onClickSave = viewModel::createMemo,
        onClickEdit = viewModel::updateMemo,
        setLocation = {
            viewModel.setLocation(it)
            viewModel.setCoords(memo.coords)
        },
        setImage = viewModel::setImage,
        isConnectNetwork = isConnectNetwork,
    )
}

private fun checkAndRequestPermissions(
    context: Context,
    permissions: Array<String>,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
) {
    if (permissions.any {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }) {
        launcher.launch(permissions)
    }
}

@Composable
private fun MemoCreateScreen(
    uiState: MemoCreateUiState = MemoCreateUiState.Initial,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = { },
    memo: MemoUI = MemoUI(),
    navigateToGallery: () -> Unit = { },
    navigateToLocationSetting: () -> Unit = { },
    navigateToMemoDetail: (MemoUI) -> Unit = { },
    setTitle: (String) -> Unit = { },
    setSize: (String) -> Unit = { },
    setDate: (String) -> Unit = { },
    setContent: (String) -> Unit = { },
    setFishType: (String) -> Unit = { },
    setWaterType: (String) -> Unit = { },
    onClickSave: () -> Unit = { },
    onClickEdit: () -> Unit = { },
    setLocation: (String) -> Unit = { },
    setImage: (String) -> Unit = { },
    isConnectNetwork: Boolean = true,
) {
    var isShowCalendar by remember {
        mutableStateOf(false)
    }

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    LaunchedEffect(key1 = keyboardHeight) {
        coroutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }

    Box(
        modifier = modifier
            .imePadding()
    ) {

        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .windowInsetsPadding(WindowInsets.safeContent.only(WindowInsetsSides.Bottom + WindowInsetsSides.Top)),
            horizontalAlignment = CenterHorizontally,
        ) {
            if (uiState is MemoCreateUiState.Success) {
                navigateToMemoDetail(uiState.memo)
            }

            if (isShowCalendar) {
                FMCalendarDialog(
                    selection = stringResource(id = R.string.selection),
                    cancel = stringResource(id = com.qure.feature.memo.R.string.cancel),
                    onDismissRequest = { isShowCalendar = false },
                    onDateSelected = setDate,
                    date = memo.date,
                )
            }

            FMTopAppBar(
                onBack = onBack,
            )
            if (isConnectNetwork) {
                MemoItem(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    contentModifier = Modifier
                        .padding(top = 10.dp)
                        .height(45.dp)
                        .fillMaxWidth(),
                    title = stringResource(id = R.string.title),
                    hint = stringResource(id = R.string.input_title),
                    contentMode = MemoMode.INPUT,
                    value = memo.title,
                    onValueChange = setTitle,
                )
                MemoItem(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    contentModifier = Modifier
                        .height(130.dp)
                        .padding(top = 15.dp)
                        .fillMaxWidth(),
                    title = stringResource(id = R.string.image),
                    contentMode = MemoMode.IMAGE,
                    onClick = { navigateToGallery() },
                    value = memo.image,
                    onValueChange = setImage
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 10.dp),
                ) {
                    FMChipGroup(
                        elements = listOf(
                            stringResource(id = R.string.fresh_water),
                            stringResource(id = R.string.sea_water),
                        ),
                        onClickChip = setWaterType,
                        selectedChip = memo.waterType,
                        unSelectedFontColor = MaterialTheme.colorScheme.onBackground,
                        selectedFontColor = White,
                    )
                    MemoSizeInputItem(
                        modifier = Modifier
                            .width(200.dp)
                            .padding(end = 5.dp),
                        onValueChange = setSize,
                        size = memo.fishSize,
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                ) {
                    MemoItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(2f)
                            .padding(end = 10.dp),
                        contentModifier = Modifier
                            .padding(top = 10.dp)
                            .height(40.dp)
                            .fillMaxWidth(),
                        title = stringResource(id = R.string.location),
                        icon = {
                            Icon(
                                painter = painterResource(id = com.qure.core.designsystem.R.drawable.ic_marker),
                                contentDescription = null,
                                tint = Color(0xFF056AEE)
                            )
                        },
                        contentMode = MemoMode.TEXT,
                        onClick = { navigateToLocationSetting() },
                        value = memo.location,
                        onValueChange = setLocation,
                    )
                    MemoItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(start = 10.dp),
                        contentModifier = Modifier
                            .padding(top = 10.dp)
                            .height(40.dp)
                            .fillMaxWidth(),
                        title = stringResource(id = R.string.date),
                        icon = {
                            Icon(
                                painter = painterResource(id = com.qure.core.designsystem.R.drawable.ic_date),
                                contentDescription = null,
                                tint = Color(0xFFF38315)
                            )
                        },
                        value = memo.date,
                        contentMode = MemoMode.TEXT,
                        onClick = { isShowCalendar = true },
                        onValueChange = setDate,
                    )
                }
                MemoItem(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    contentModifier = Modifier
                        .padding(top = 10.dp)
                        .height(45.dp)
                        .fillMaxWidth(),
                    title = stringResource(id = R.string.fish_type),
                    hint = stringResource(id = R.string.input_fishtype),
                    contentMode = MemoMode.INPUT,
                    value = memo.fishType,
                    onValueChange = setFishType,
                )

                MemoItem(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    contentModifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .height(80.dp),
                    title = stringResource(id = R.string.content),
                    hint = stringResource(id = R.string.input_content),
                    contentMode = MemoMode.INPUT,
                    value = memo.content,
                    onValueChange = setContent,
                )

                Spacer(modifier = Modifier.weight(1f))

                FMButton(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .padding(bottom = 20.dp)
                        .fillMaxWidth(),
                    onClick = {
                        if (memo.uuid.isNotEmpty()) {
                            onClickEdit()
                        } else {
                            onClickSave()
                        }
                    },
                    text = if (memo.uuid.isNotEmpty()) {
                        stringResource(id = R.string.edit)
                    } else stringResource(
                        id = R.string.save
                    ),
                    textStyle = MaterialTheme.typography.displayMedium,
                    buttonColor = Blue500,
                    fontColor = White,
                    shape = RoundedCornerShape(10.dp),
                    isEnabled = memo.isValidMemo,
                )
            } else {
                OfflineView(
                    modifier = Modifier
                        .padding(top = 100.dp),
                )
            }

        }
        val isLoading = uiState is MemoCreateUiState.Loading
        if (isLoading) {
            FMProgressBar(
                modifier = modifier
                    .align(Alignment.Center),
            )
        }
    }
}



@Composable
private fun MemoSizeInputItem(
    modifier: Modifier = Modifier,
    size: String = "",
    onValueChange: (String) -> Unit = { },
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
    ) {
        Column(
            modifier = Modifier
                .width(100.dp)
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.Bottom,
        ) {
            BasicTextField(
                value = size,
                onValueChange = onValueChange,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                textStyle = MaterialTheme.typography.displaySmall.copy(
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground)
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onBackground,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
            )
        }
        Text(
            modifier = Modifier
                .align(Alignment.Bottom)
                .padding(start = 5.dp),
            text = stringResource(id = R.string.size),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp,
        )

    }
}

@Composable
private fun MemoItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    title: String = "",
    hint: String = "",
    value: String = "",
    onValueChange: (String) -> Unit = { },
    contentModifier: Modifier = Modifier,
    icon: @Composable () -> Unit = { },
    contentMode: MemoMode = MemoMode.INPUT,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon()
            Text(
                text = title,
                fontSize = 16.sp,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        when (contentMode) {
            MemoMode.INPUT -> {
                Box(
                    modifier = contentModifier
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(10.dp),
                        ),
                ) {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = contentModifier
                            .padding(start = 10.dp)
                            .fillMaxSize()
                            .align(Alignment.CenterStart),
                        textStyle = MaterialTheme.typography.displaySmall.copy(
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        decorationBox = { innerTextField ->
                            if (value.isEmpty()) {
                                Text(
                                    text = hint,
                                    style = TextStyle(
                                        color = Gray100,
                                    ),
                                    fontSize = 18.sp,
                                )
                            }
                            innerTextField()
                        },
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                    )
                }
            }

            MemoMode.IMAGE -> {
                Box(
                    modifier = contentModifier
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(10.dp),
                        )
                        .clickable { onClick() },
                ) {
                    val imageModifier = if (value.isEmpty()) {
                        Modifier
                            .size(100.dp)
                            .align(Alignment.Center)
                    } else {
                        Modifier.fillMaxSize()
                    }
                    FMGlideImage(
                        modifier = imageModifier,
                        model = if (value.isEmpty()) com.qure.core.designsystem.R.drawable.ic_image else value,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                    )
                }
            }

            MemoMode.TEXT -> {
                Box(
                    modifier = contentModifier
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(10.dp),
                        )
                        .clickable { onClick() },
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(horizontal = 10.dp),
                        text = value,
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        style = MaterialTheme.typography.displaySmall,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        }
    }
}

enum class MemoMode {
    INPUT,
    IMAGE,
    TEXT,
}

@Preview(showBackground = true)
@Composable
private fun MemoCreateContentPreview() = FMPreview {
    MemoCreateScreen()
}
