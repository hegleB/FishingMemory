package com.qure.gallery


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import com.qure.core.BaseActivity
import com.qure.core.util.FishingMemoryToast
import com.qure.core.util.setOnSingleClickListener
import com.qure.core_design.custom.recyclerview.RecyclerViewItemDecoration
import com.qure.domain.DEFAULT_GALLERY_REQUEST_CODE
import com.qure.domain.PHOTO_FILE
import com.qure.gallery.databinding.ActivityGalleryBinding
import com.qure.navigator.MemoCreateNavigator
import dagger.hilt.android.AndroidEntryPoint
import java.io.*
import javax.inject.Inject

@AndroidEntryPoint
class GalleryActivity : BaseActivity<ActivityGalleryBinding>(R.layout.activity_gallery),
    GalleryAdapter.OnItemClickListener {

    @Inject
    lateinit var memoCreateNavigator: MemoCreateNavigator

    private lateinit var adapter: GalleryAdapter
    private var images: List<GalleryImage> = emptyList()
    private var selectedImage: Uri? = null
    private var preSelectedImage: Uri? = null


    private lateinit var itemListener: GalleryAdapter.OnItemClickListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemListener = this
        images = loadGalleryImages()
        initView()
        initRecyclerView()
    }

    private fun initView() {
        binding.textViewActivityGalleryDone.setOnClickListener {
            val intent = Intent()
            intent.putExtra(PHOTO_FILE, selectedImage)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        binding.imageViewActivityGalleryClose.setOnSingleClickListener {
            finish()
        }


    }

    private fun initRecyclerView() {
        adapter = GalleryAdapter(this, images, itemListener)
        binding.recyclerViewActivityGalleryImages.adapter = adapter
        binding.recyclerViewActivityGalleryImages.addItemDecoration(RecyclerViewItemDecoration(10))
        adapter.submitList(images)
    }

    private fun loadGalleryImages(): List<GalleryImage> {
        val images = mutableListOf<GalleryImage>()
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA
        )

        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val pathColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val path = it.getString(pathColumn)
                images.add(GalleryImage(id, path))
            }
        }

        return images
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            DEFAULT_GALLERY_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamera()
                } else {
                    FishingMemoryToast().error(this, "권한이 거부되었습니다.")
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DEFAULT_GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

            val uri = Uri.parse(getImageUri(imageBitmap).toString())
            val file = copyUriToExternalStorage(uri)

            val intent = memoCreateNavigator.intent(this)

            intent.putExtra(PHOTO_FILE, Uri.fromFile(file))
            setResult(Activity.RESULT_OK, intent)
            finish()

        }
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
        val path = MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            System.currentTimeMillis().toString(),
            null
        )
        return Uri.parse(path)
    }


    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, DEFAULT_GALLERY_REQUEST_CODE)
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            ),
            DEFAULT_GALLERY_REQUEST_CODE
        )
    }


    override fun setOnItemClickListener(uri: Uri) {
        binding.textViewActivityGalleryDone.isEnabled =
            if (preSelectedImage == uri && binding.textViewActivityGalleryDone.isEnabled) false else true
        preSelectedImage = uri
        selectedImage = uri
    }

    override fun setOnItemClickListener() {
        if (checkPermission()) {
            startCamera()
        } else {
            requestPermission()
        }
    }
}