package com.qure.create

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.qure.core.BaseActivity
import com.qure.core.extensions.Empty
import com.qure.core.extensions.gone
import com.qure.core.extensions.visiable
import com.qure.core.util.FishingMemoryToast
import com.qure.core.util.setOnSingleClickListener
import com.qure.create.databinding.ActivityMemoCreateBinding
import com.qure.create.location.LocationSettingActivity
import com.qure.domain.REQUEST_CODE_AREA
import com.qure.domain.UPDATE_MEMO
import com.qure.domain.entity.auth.*
import com.qure.domain.entity.memo.*
import com.qure.history.MemoCalendarDialogFragment
import com.qure.memo.detail.DetailMemoActivity
import com.qure.memo.model.MemoUI
import com.qure.navigator.DetailMemoNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject


@AndroidEntryPoint
class MemoCreateActivity : BaseActivity<ActivityMemoCreateBinding>(R.layout.activity_memo_create),
    MemoCalendarDialogFragment.DatePickerListener {

    @Inject
    lateinit var detailMemoNavigator: DetailMemoNavigator

    private val viewModel by viewModels<MemoViewModel>()
    private var selectedImageUri: Uri? = null

    lateinit var listener: MemoCalendarDialogFragment.DatePickerListener

    private var createdMemo: MemoUI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener = this
        createdMemo = intent.getParcelableExtra(UPDATE_MEMO)
        initView()
        setDate()
        observe()
        setCreatedMemo()
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
            startActivityForResult(
                Intent(this, LocationSettingActivity::class.java),
                REQUEST_CODE_AREA
            )
        }

        binding.buttonActivityMemoCreatePost.setOnSingleClickListener {
            saveMemo()
            uploadImage()
        }

        validateMemo()
    }

    private fun setCreatedMemo() {
        createdMemo?.let { memo ->
            with(binding) {
                editTextActivityMemoCreateTitle.setText(memo.title)
                editTextActivityMemoCreateFishSize.setText(memo.fishSize)
                textViewActivityMemoCreateLocationInfo.setText(memo.location)
                textViewActivityMemoCreateDate.setText(memo.date)
                editTextActivityMemoCreateFishType.setText(memo.fishType)
                editTextActivityMemoCreateContent.setText(memo.content)
                setSelectedChipText(chipGroupActivityMemoCreateType, memo.waterType)
                buttonActivityMemoCreatePost.setText(getString(R.string.edit))
                Glide.with(this@MemoCreateActivity)
                    .load(memo.image)
                    .transform(CenterCrop(), RoundedCorners(15))
                    .override(360, 360)
                    .into(imageViewActivityMemoCreateFishImage)
                Handler().postDelayed({
                    checkInputs()
                }, 100)
            }
        }
    }

    private fun setSelectedChipText(chipGroup: ChipGroup, selectedText: String?) {
        selectedText?.let { text ->
            for (i in 0 until chipGroup.childCount) {
                val chip = chipGroup.getChildAt(i) as Chip
                if (chip.text.toString() == text) {
                    chipGroup.check(chip.id)
                    break
                }
            }
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

    private fun uploadImage() {
        if (selectedImageUri != null) {
            val parcelFileDescriptor = contentResolver.openFileDescriptor(
                selectedImageUri!!, "r", null
            ) ?: return

            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            viewModel.setImage(file)
            if (createdMemo == null) {
                viewModel.uploadMemoImage()
            } else {
                viewModel.uploadMemoImage(createdMemo!!.uuid)
            }
        } else {
            viewModel.updateMemo(createdMemo!!.uuid, createdMemo!!.image)
            finish()
        }
    }

    private fun saveMemo() {
        val chipGroup = binding.chipGroupActivityMemoCreateType
        val selectedChipId = binding.chipGroupActivityMemoCreateType.checkedChipId
        val selectedChip: Chip = chipGroup.findViewById(selectedChipId)
        with(viewModel) {
            setTitle(binding.editTextActivityMemoCreateTitle.text.toString())
            setWaterType(selectedChip.text.toString())
            setFishType(binding.editTextActivityMemoCreateFishType.text.toString())
            setFishSize(binding.editTextActivityMemoCreateFishSize.text.toString())
            setLocation(binding.textViewActivityMemoCreateLocationInfo.text.toString())
            setDate(binding.textViewActivityMemoCreateDate.text.toString())
            setContent(binding.editTextActivityMemoCreateContent.text.toString())
        }
    }

    private fun observe() {
        viewModel.error
            .onEach { errorMessage ->
                FishingMemoryToast().show(
                    this,
                    errorMessage,
                )
            }.launchIn(lifecycleScope)


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect {
                        handleSaveOrUpdateState(it)
                    }
                }
            }
        }

    }

    private fun handleSaveOrUpdateState(it: UiState) {
        when {
            it.isUploadImage && !it.isSave && !it.isUpdated -> binding.progressBarActivityMemo.visiable()
            it.isUploadImage && it.isUpdated -> {
                sendSuccessMessage(R.string.toast_update_memo)
                val intent = detailMemoNavigator.intent(this).apply {
                    putExtra(UPDATE_MEMO, it.memo)
                }
                startActivity(intent)
            }
            it.isUploadImage && it.isSave -> sendSuccessMessage(R.string.toast_save_memo)
            else -> binding.progressBarActivityMemo.gone()
        }
    }

    private fun sendSuccessMessage(stringdId: Int) {
        FishingMemoryToast().show(
            this@MemoCreateActivity,
            getString(stringdId)
        )
        finish()
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
            .setTitle(getString(R.string.permission_required))
            .setMessage(getString(R.string.required_to_import_image))
            .setPositiveButton(getString(R.string.agreement)) { _, _ ->
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

    private fun validateMemo() {
        with(binding) {
            editTextActivityMemoCreateTitle.addTextChangedListener(textWatcher)
            editTextActivityMemoCreateFishSize.addTextChangedListener(textWatcher)
            editTextActivityMemoCreateFishType.addTextChangedListener(textWatcher)
            textViewActivityMemoCreateLocationInfo.addTextChangedListener(textWatcher)
            textViewActivityMemoCreateDate.addTextChangedListener(textWatcher)
            imageViewActivityMemoCreateFishImage.addOnAttachStateChangeListener()
            chipGroupActivityMemoCreateType.setOnCheckedChangeListener()
        }
    }

    private fun checkInputs() {
        with(binding) {
            val title = editTextActivityMemoCreateTitle.text.toString()
            val fishType = editTextActivityMemoCreateFishType.text.toString()
            val fishSize = editTextActivityMemoCreateFishSize.text.toString()
            val locationInfo = textViewActivityMemoCreateLocationInfo.text.toString()
            val date = textViewActivityMemoCreateDate.text.toString()
            val isChecked = chipGroupActivityMemoCreateType.checkedChipId != View.NO_ID
            val hasImage = imageViewActivityMemoCreateFishImage.drawable != null

            buttonActivityMemoCreatePost.isEnabled =
                title.isNotBlank() &&
                        fishType.isNotBlank() &&
                        fishSize.isNotBlank() &&
                        locationInfo.isNotBlank() &&
                        date.isNotBlank() &&
                        isChecked &&
                        hasImage
        }
    }

    private fun ChipGroup.setOnCheckedChangeListener() {
        this.setOnCheckedStateChangeListener { group, checkedIds ->
            checkInputs()
        }
    }

    private fun ImageView.addOnAttachStateChangeListener() {
        this.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                checkInputs()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        override fun afterTextChanged(s: Editable?) {
            checkInputs()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == REQUEST_CODE_AREA && data != null -> {
                binding.textViewActivityMemoCreateLocationInfo.text =
                    data.getStringExtra(ARG_AREA)
                viewModel.setCoords(data.getStringExtra(ARG_AREA_COORDS) ?: String.Empty)
            }

            requestCode == DEFAULT_GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null -> {
                val data = data.data as Uri
                selectedImageUri = data

                Glide.with(this)
                    .load(data)
                    .transform(CenterCrop(), RoundedCorners(15))
                    .override(360, 360)
                    .into(binding.imageViewActivityMemoCreateFishImage)
                Handler().postDelayed({
                    checkInputs()
                }, 100)
            }
            else ->
                Snackbar.make(
                    binding.constraintLayoutActivityMemoCreate,
                    getString(R.string.can_not_get_image),
                    Snackbar.LENGTH_LONG
                )
        }
    }

    private fun ContentResolver.getFileName(selectedImageUri: Uri): String {
        var name = ""
        val returnCursor = this.query(selectedImageUri, null, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = System.currentTimeMillis().toString() + returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 1000
        const val DEFAULT_GALLERY_REQUEST_CODE = 1002
        const val ARG_AREA = "ARG_AREA"
        const val ARG_AREA_COORDS = "ARG_AREA_COORDS"
    }
}


