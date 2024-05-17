package com.qure.gallery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import com.qure.core.BaseComposeActivity
import com.qure.core_design.compose.theme.FishingMemoryTheme
import com.qure.domain.PHOTO_FILE
import com.qure.navigator.MemoCreateNavigator
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class GalleryActivity : BaseComposeActivity() {
    @Inject
    lateinit var memoCreateNavigator: MemoCreateNavigator
    @Composable
    override fun Screen() {
        FishingMemoryTheme {
            GalleryScreen(
                onClickClose = { finish() },
                onClickCamera = { imageBitmap ->
                    navigateToMemoCreate(imageBitmap)
                },
                onClickDone = { galleryImage ->
                    val imageBitmap = BitmapFactory.decodeFile(galleryImage.path)
                    navigateToMemoCreate(imageBitmap)
                },
            )
        }
    }
    private fun navigateToMemoCreate(imageBitmap: Bitmap) {
        val uri = Uri.parse(getImageUri(imageBitmap).toString())
        val file = copyUriToExternalStorage(uri)
        val intent = memoCreateNavigator.intent(this)
        intent.putExtra(PHOTO_FILE, Uri.fromFile(file))
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun copyUriToExternalStorage(uri: Uri): File {
        val parcelFileDescriptor: ParcelFileDescriptor =
            this.contentResolver.openFileDescriptor(uri, "r")
                ?: throw IllegalArgumentException("파일 변환 실패")
        val fileDescriptor: FileDescriptor = parcelFileDescriptor.fileDescriptor
        val inputStream = FileInputStream(fileDescriptor)
        val directory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val fileName = "${System.currentTimeMillis()}.jpg"
        val file = File(directory, fileName)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        return file
    }

    private fun getImageUri(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(
                contentResolver,
                bitmap,
                System.currentTimeMillis().toString(),
                null,
            )
        return Uri.parse(path)
    }
}
