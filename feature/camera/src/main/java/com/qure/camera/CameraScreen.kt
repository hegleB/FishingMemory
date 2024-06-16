package com.qure.camera

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qure.camera.utils.ComposeFileProvider
import com.qure.designsystem.theme.Blue500
import com.qure.designsystem.theme.Blue600
import com.qure.designsystem.theme.Gray500
import com.qure.designsystem.theme.White
import com.qure.feature.camera.R
import com.qure.model.camera.ObjectRect
import com.qure.ui.component.CameraFrameCorners
import com.qure.ui.component.LevelIndicatorView
import com.qure.ui.component.RectangleView
import com.qure.ui.model.MemoUI
import kotlinx.coroutines.launch

@Composable
fun CameraRoute(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    memoUI: MemoUI,
    navigateToMemoCreate: (MemoUI) -> Unit,
    viewModel: CameraViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val cameraUiState by viewModel.cameraUiState.collectAsStateWithLifecycle()
    val resource = context.resources

    val activity = LocalContext.current as ComponentActivity

    DisposableEffect(Unit) {
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        onDispose {
            activity.requestedOrientation = originalOrientation
        }
    }

    CameraScreen(
        context = context,
        cameraUiState = cameraUiState,
        setCropImage = { bitmap ->
            val uri = ComposeFileProvider.getImageUri(context, bitmap)
            val imagePath = uri.path?.replace("/my_images/", "")
            imagePath?.let { path ->
                navigateToMemoCreate(
                    memoUI.copy(
                        image = resource.getString(R.string.image_path, path),
                        fishSize = cameraUiState.bodySize.toString()
                    )
                )
            }
        },
        sendMessage = { message ->
            onShowErrorSnackBar(Throwable(message))
        },
        setDetectedRect = viewModel::setDetectedRect,
        changeLevel = viewModel::changeLevel,
    )
}

@OptIn(ExperimentalGetImage::class)
@SuppressLint("RestrictedApi")
@Composable
fun CameraScreen(
    context: Context,
    cameraUiState: CameraUiState = CameraUiState(),
    setCropImage: (Bitmap) -> Unit,
    sendMessage: (String) -> Unit,
    setDetectedRect: (List<ObjectRect<Int>>) -> Unit,
    changeLevel: (Float, Float) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraManager = remember { CameraFactory.create() }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    LaunchedEffect(Unit) {
        cameraManager.initialize(context)
        previewView = cameraManager.getPreviewView()
    }

    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                coroutineScope.launch {
                    cameraManager.startCamera(lifecycleOwner)
                    cameraManager.fetchImageAnalyzer(setDetectedRect)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        previewView?.let { preview ->
            AndroidView(
                factory = {
                    preview
                },
                modifier = Modifier
                    .fillMaxSize(),
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 100.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Image(
                modifier = Modifier
                    .size(120.dp),
                painter = painterResource(com.qure.core.designsystem.R.drawable.ic_fish),
                contentDescription = null,
            )

            Spacer(modifier = Modifier.height(100.dp))
            Image(
                modifier = Modifier
                    .size(120.dp),
                painter = painterResource(com.qure.core.designsystem.R.drawable.ic_credit_card),
                contentDescription = null,
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 20.dp, top = 10.dp)
                .background(
                    color = when {
                        cameraUiState.isLevelCorrect.not() -> Color.Red
                        else -> Color(0x800000FF)
                    }
                ),
        ) {
            Text(
                text = stringResource(
                    id =
                    when {
                        cameraUiState.isLevelCorrect.not() -> R.string.message_level
                        cameraUiState.fishSize == null -> R.string.no_object_detected
                        cameraUiState.cardSize == null -> R.string.single_object_detected
                        else -> R.string.measuring_fish_size
                    }
                ),
                fontSize = 20.sp,
                color = White
            )
        }

        if (cameraUiState.isLevelCorrect) {
            cameraUiState.fishSize?.let { fishRect ->
                RectangleView(
                    modifier = Modifier
                        .fillMaxSize(),
                    borderColor = Blue600,
                    size = fishRect,
                )
            }

            cameraUiState.cardSize?.let { cardRect ->
                RectangleView(
                    modifier = Modifier
                        .fillMaxSize(),
                    borderColor = Color.Yellow,
                    size = cardRect,
                )
            }
        }

        CameraFrameCorners(
            modifier = Modifier
                .align(Alignment.Center),
        )

        LevelIndicatorView(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.BottomStart)
                .padding(bottom = 10.dp, end = 10.dp),
            changeLevel = changeLevel,
        )

        IconButton(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(bottom = 30.dp),
            onClick = {
                cameraUiState.fishSize?.let { fishSize ->
                    cameraManager.takePicture(
                        objectRect = fishSize,
                        setCropImage = setCropImage,
                        sendMessage = sendMessage,
                    )
                }
            },
            enabled = cameraUiState.bodySize != null && cameraUiState.isLevelCorrect,
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .background(
                        shape = CircleShape,
                        color = if (cameraUiState.bodySize != null && cameraUiState.isLevelCorrect) Blue500 else Gray500
                    ),
            )
        }
    }
}