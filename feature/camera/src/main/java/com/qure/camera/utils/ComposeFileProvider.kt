package com.qure.camera.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import com.qure.feature.camera.R

import java.io.File
import java.io.FileOutputStream

class ComposeFileProvider : FileProvider(
    R.xml.file_paths,
) {
    companion object {
        fun getImageUri(context: Context, bitmap: Bitmap): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File.createTempFile(
                "selected_image_",
                ".jpg",
                directory,
            )
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
            val authority = context.packageName + ".provider"
            return getUriForFile(
                context,
                authority,
                file,
            )
        }
    }
}