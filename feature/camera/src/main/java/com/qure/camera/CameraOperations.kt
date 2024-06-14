package com.qure.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import androidx.camera.core.ImageProxy
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.qure.model.camera.ObjectRect

interface CameraOperations {
    fun initialize(context: Context)
    suspend fun startCamera(lifecycleOwner: LifecycleOwner)
    fun takePicture(
        objectRect: ObjectRect<Int>?,
        setCropImage: (Bitmap) -> Unit,
        sendMessage: (String) -> Unit,
    )
    fun unBindCamera()
    fun getPreviewView(): PreviewView
    fun detectImage(
        imageProxy: ImageProxy,
        successCallback: (List<Rect>) -> Unit
    )
    fun fetchImageAnalyzer(
        sendDetectedImage: (List<ObjectRect<Int>>) -> Unit
    )
}