package com.qure.camera

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.util.Size
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.qure.camera.utils.getCameraProvider
import com.qure.camera.utils.heightConvert
import com.qure.camera.utils.toByteArray
import com.qure.camera.utils.widthConvert
import com.qure.model.camera.ObjectRect
import com.qure.ui.model.SnackBarMessageType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.math.abs

class CameraManager : CameraOperations {

    private lateinit var context: Context
    private lateinit var resources: Resources
    private lateinit var screenSize: Size
    private lateinit var previewView: PreviewView
    private lateinit var preview: Preview
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var provider: ProcessCameraProvider
    private lateinit var cameraxSelector: CameraSelector
    private lateinit var imageCapture: ImageCapture
    private lateinit var imageAnalyzer: ImageAnalysis
    private lateinit var cameraProvider: ProcessCameraProvider

    @SuppressLint("RestrictedApi")
    override fun initialize(context: Context) {
        this.context = context
        resources = context.resources
        screenSize = Size(
            resources.displayMetrics.widthPixels,
            resources.displayMetrics.heightPixels,
        )
        preview = Preview.Builder()
            .build()
        previewView = PreviewView(context)
        cameraxSelector =
            CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
        cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        provider = cameraProviderFuture.get()
        imageCapture = ImageCapture.Builder()
            .setDefaultResolution(screenSize)
            .build()
        imageAnalyzer = ImageAnalysis.Builder()
            .setDefaultResolution(screenSize)
            .build()
    }

    override suspend fun startCamera(
        lifecycleOwner: LifecycleOwner,
    ) {
        unBindCamera()
        cameraProvider = context.getCameraProvider()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraxSelector,
            preview,
            imageCapture,
            imageAnalyzer
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    override fun takePicture(
        objectRect: ObjectRect<Int>?,
        setCropImage: (Bitmap) -> Unit,
        sendMessage: (SnackBarMessageType) -> Unit,
    ) {
        CoroutineScope(Dispatchers.Default + SupervisorJob()).launch {
            imageCapture.takePicture(
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onError(exc: ImageCaptureException) {
                        sendMessage(SnackBarMessageType.CAMERA_CAPTURE_FAILURE)
                    }

                    override fun onCaptureSuccess(image: ImageProxy) {
                        val buffer = image.planes[0].buffer
                        val data = buffer.toByteArray()
                        val origin = BitmapFactory.decodeByteArray(data, 0, data.size)
                        objectRect?.let { rect ->
                            val widthRatio = image.width.toFloat() / screenSize.width
                            val heightRatio = image.height.toFloat() / screenSize.height

                            val margin = 100

                            val top = maxOf((rect.top * heightRatio).toInt() - margin, 0)
                            val bottom = (rect.bottom * heightRatio).toInt()
                            val left = maxOf((rect.left * widthRatio).toInt() - margin, 0)
                            val right = (rect.right * widthRatio).toInt() + margin

                            try {
                                val crop = Bitmap.createBitmap(
                                    origin,
                                    left,
                                    top,
                                    abs(right - left),
                                    abs(bottom - top),
                                )

                                setCropImage(crop)
                                sendMessage(SnackBarMessageType.CAMERA_CAPTURE_SUCCESS)
                                image.close()
                            } catch (e: Exception) {
                                sendMessage(SnackBarMessageType.CAMERA_CAPTURE_FAILURE)
                                return

                            }
                        } ?: run {
                            sendMessage(SnackBarMessageType.CAMERA_CAPTURE_DETECT_FAILURE)
                            image.close()
                        }
                    }
                })
        }
    }

    override fun unBindCamera() {
        provider.unbindAll()
    }

    override fun getPreviewView(): PreviewView = previewView

    @OptIn(ExperimentalGetImage::class)
    override fun detectImage(
        imageProxy: ImageProxy,
        successCallback: (List<Rect>) -> Unit
    ) {
        val option = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()
        val detector = ObjectDetection.getClient(option)

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val process = detector.process(image)

            process.addOnSuccessListener { detectedObject ->
                successCallback(detectedObject.map { it.boundingBox })
            }

            process.addOnCompleteListener {
                imageProxy.close()
            }
        }
    }

    @OptIn(ExperimentalGetImage::class)
    override fun fetchImageAnalyzer(
        sendDetectedImage: (List<ObjectRect<Int>>) -> Unit,
    ) {
        imageAnalyzer.also { imageAnalysis ->
            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                detectImage(imageProxy) { detectedRect ->
                    imageProxy.image?.let { image ->
                        sendDetectedImage(detectedRect.map { rect ->
                            ObjectRect(
                                heightConvert(rect.top, image.height, screenSize.height),
                                heightConvert(rect.bottom, image.height, screenSize.height),
                                widthConvert(rect.left, image.width, screenSize.width),
                                widthConvert(rect.right, image.width, screenSize.width),
                            )
                        }
                        )
                    }
                }
            }
        }
    }

}