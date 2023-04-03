package com.qure.create

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.snackbar.Snackbar
import com.qure.core.BaseActivity
import com.qure.create.databinding.ActivityMemoCreateBinding
import com.qure.create.location.LocationSettingActivity
import com.qure.history.MemoCalendarDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemoCreateActivity : BaseActivity<ActivityMemoCreateBinding>(R.layout.activity_memo_create),
    MemoCalendarDialogFragment.DatePickerListener {

    lateinit var listener: MemoCalendarDialogFragment.DatePickerListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener = this

        initView()
        setDate()
    }

    private fun initView() {
        binding.imageViewActivityMemoCreateBack.setOnClickListener {
            finish()
        }

        binding.imageViewActivityMemoCreateFishImage.setOnClickListener {
            checkExternalStoragePermission {
                startGallery()
            }
        }
        binding.textViewActivityMemoCreateLocationInfo.setOnClickListener {
            startActivity(Intent(this, LocationSettingActivity::class.java))
        }
    }

    private fun setDate() {
        binding.textViewActivityMemoCreateDate.setOnClickListener {
            MemoCalendarDialogFragment(listener).show(
                supportFragmentManager,
                MemoCalendarDialogFragment.TAG
            )
        }
    }

    override fun selectDate(date: String) {
        binding.textViewActivityMemoCreateDate.text = date
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkExternalStoragePermission(uploadAction: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                uploadAction()
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                showPermissionContextPopup()
            }
            else -> {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("사진을 가져오기 위해 필요합니다.")
            .setPositiveButton("동의") { _, _ ->
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
            .create()
            .show()
    }

    private fun startGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, DEFAULT_GALLERY_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startGallery()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != Activity.RESULT_OK && data != null) {
            val uri = data.data as Uri
            Glide.with(this)
                .load(uri)
                .transform(CenterCrop(), RoundedCorners(15))
                .into(binding.imageViewActivityMemoCreateFishImage)
        } else {
            Snackbar.make(
                binding.constraintLayoutActivityMemoCreate,
                "사진을 가져오지 못했습니다.",
                Snackbar.LENGTH_LONG
            )
        }
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 1000
        const val GALLERY_REQUEST_CODE = 1001
        const val DEFAULT_GALLERY_REQUEST_CODE = 1002
    }
}