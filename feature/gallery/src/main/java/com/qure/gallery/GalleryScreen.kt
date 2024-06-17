package com.qure.gallery

import android.Manifest
import android.content.Context
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.qure.designsystem.component.FMCloseButton
import com.qure.designsystem.component.FMGlideImage
import com.qure.designsystem.theme.Blue600
import com.qure.designsystem.theme.Gray200
import com.qure.designsystem.utils.FMPreview
import com.qure.feature.gallery.R
import com.qure.model.gallery.GalleryImage
import com.qure.ui.model.MemoUI
import com.qure.ui.model.SnackBarMessageType
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GalleryRoute(
    memoUI: MemoUI = MemoUI(),
    onBack: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onClickCamera: (MemoUI) -> Unit,
    onClickDone: (MemoUI) -> Unit,
    onShowMessageSnackBar: (messageType: SnackBarMessageType) -> Unit,
    viewModel: GalleryViewModel = hiltViewModel(),
) {

    LaunchedEffect(viewModel.error) {
        viewModel.error.collectLatest(onShowErrorSnackBar)
    }

    val context = LocalContext.current
    val images = remember { mutableStateListOf(GalleryImage(0, "")) }
    loadGalleryImages(context = context, images = images)

    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )
    val readPermissionState = rememberPermissionState(
        permission = Manifest.permission.READ_EXTERNAL_STORAGE
    )
    val writePermissionState = rememberPermissionState(
        permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted.not()) {
            onShowMessageSnackBar(SnackBarMessageType.PERMISSION_FAILURE)
        }
    }

    LaunchedEffect(cameraPermissionState) {
        if (cameraPermissionState.status.isGranted.not()) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(readPermissionState) {
        if (readPermissionState.status.isGranted.not()) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    LaunchedEffect(writePermissionState) {
        if (writePermissionState.status.isGranted.not()) {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    GalleryScreen(
        onClickClose = onBack,
        images = images,
        onClickDone = { onClickDone(memoUI.copy(image = it.path)) },
        onClickTakingPicture = { onClickCamera(memoUI) },
    )
}

private fun loadGalleryImages(
    context: Context,
    images: MutableList<GalleryImage>
) {
    val externalDirs = ContextCompat.getExternalFilesDirs(context, null)
    externalDirs.forEach { externalDir ->
        externalDir?.let {
            val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA)
            val cursor =
                context.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    null,
                )
            cursor?.let {
                while (it.moveToNext()) {
                    val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    val idColumn = it.getColumnIndex(MediaStore.Images.Media._ID)
                    val imagePath = it.getString(columnIndex)
                    val id = it.getLong(idColumn)
                    images.add(GalleryImage(id = id, path = imagePath))
                }
                it.close()
            }
        }
    }
}

@Composable
private fun GalleryScreen(
    modifier: Modifier = Modifier,
    onClickClose: () -> Unit = { },
    onClickTakingPicture: () -> Unit = { },
    images: List<GalleryImage> = emptyList(),
    onClickDone: (GalleryImage) -> Unit = { },
) {
    var selectedImage by remember {
        mutableStateOf(GalleryImage(0, ""))
    }
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .padding(top = 30.dp),
        ) {
            FMCloseButton(
                modifier = modifier
                    .align(Alignment.CenterStart),
                onClickClose = { onClickClose() },
                iconColor = MaterialTheme.colorScheme.onBackground,
            )
            TextButton(
                modifier = modifier
                    .align(Alignment.CenterEnd),
                onClick = { onClickDone(selectedImage) },
                enabled = selectedImage.path.isNotEmpty(),
            ) {
                Text(
                    modifier = modifier,
                    text = stringResource(id = R.string.gallery_done),
                    fontSize = 13.sp,
                    style = MaterialTheme.typography.displaySmall,
                    color = if (selectedImage.path.isEmpty()) Gray200 else MaterialTheme.colorScheme.onBackground,
                )
            }
        }
        HorizontalDivider()

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = modifier
                .fillMaxSize()
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            itemsIndexed(images) { index, image ->
                if (index == 0) {
                    GalleryCameraItem(
                        onClickTakingPicture = onClickTakingPicture,
                    )
                } else {
                    GalleryImageItem(
                        galleryImage = image,
                        onCheckChange = { selectedImage = it },
                        isChecked = image == selectedImage,
                    )
                }
            }
        }
    }
}

@Composable
private fun GalleryImageItem(
    modifier: Modifier = Modifier,
    galleryImage: GalleryImage = GalleryImage(0, ""),
    isChecked: Boolean = false,
    onCheckChange: (GalleryImage) -> Unit = { },
) {
    Box(
        modifier = modifier
            .size(130.dp)
            .clickable { onCheckChange(galleryImage) }
            .border(
                width = 2.dp,
                color = if (isChecked) Blue600 else Color.Transparent,
            ),
    ) {
        FMGlideImage(
            model = galleryImage.path,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Icon(
            modifier = modifier
                .size(24.dp)
                .padding(top = 5.dp, end = 5.dp)
                .align(Alignment.TopEnd),
            painter = if (isChecked) painterResource(id = com.qure.core.designsystem.R.drawable.ic_check_circle) else painterResource(
                id = com.qure.core.designsystem.R.drawable.ic_outline_circle
            ),
            contentDescription = null,
            tint = if (isChecked) Blue600 else Color(0xFF606060),
        )

    }
}

@Composable
private fun GalleryCameraItem(
    modifier: Modifier = Modifier,
    onClickTakingPicture: () -> Unit = { },
) {
    Box(
        modifier = modifier
            .size(130.dp)
            .clickable { onClickTakingPicture() },
    ) {
        Column(
            modifier = modifier
                .align(Alignment.Center),
        ) {
            Image(
                modifier = modifier
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = com.qure.core.designsystem.R.drawable.ic_photo_camera),
                contentDescription = null,
            )
            Text(
                modifier = modifier
                    .padding(top = 5.dp),
                text = stringResource(id = R.string.gallery_take_a_picture)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GalleryContentPreview() = FMPreview {
    GalleryScreen(
        images = listOf(
            GalleryImage(
                id = 0,
                path = "",
            ),
            GalleryImage(
                id = 1,
                path = "",
            ),
            GalleryImage(
                id = 2,
                path = "",
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun GalleryImageItemPreview() = FMPreview {
    GalleryImageItem()
}

@Preview(showBackground = true)
@Composable
private fun GalleryCameraItemPreview() = FMPreview {
    GalleryCameraItem()
}