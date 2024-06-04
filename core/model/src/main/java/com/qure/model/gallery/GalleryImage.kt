package com.qure.model.gallery

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream

@Serializable
data class GalleryImage(
    val id: Long = 0,
    val path: String = "",
)

fun GalleryImage.empty(): GalleryImage {
    return GalleryImage(
        id = 0,
        path = "",
    )
}

fun GalleryImage.toGalleryString(): String {
    return Json.encodeToString(this)
}

fun String.toGalleryImage(): GalleryImage {
    return Json.decodeFromString(this)
}

fun Bitmap.toImageUri(context: Context): Uri {
    val bytes = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path =
        MediaStore.Images.Media.insertImage(
            context.contentResolver,
            this,
            System.currentTimeMillis().toString(),
            null,
        )
    return Uri.parse(path)
}