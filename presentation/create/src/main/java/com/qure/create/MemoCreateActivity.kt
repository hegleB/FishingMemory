package com.qure.create

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.qure.core.BaseComposeActivity
import com.qure.core.extensions.Empty
import com.qure.core.extensions.getParcelableExtraData
import com.qure.core_design.compose.theme.FishingMemoryTheme
import com.qure.create.location.LocationSettingActivity
import com.qure.domain.ARG_AREA
import com.qure.domain.ARG_AREA_COORDS
import com.qure.domain.EXTRA_REQUEST_CODE
import com.qure.domain.MEMO_DATA
import com.qure.domain.PHOTO_FILE
import com.qure.domain.REQUEST_CODE_AREA
import com.qure.domain.REQUEST_IMAGE_CAPTURE
import com.qure.domain.REQUEST_UPDATE_MEMO
import com.qure.memo.model.MemoUI
import com.qure.navigator.DetailMemoNavigator
import com.qure.navigator.GalleryNavigator
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MemoCreateActivity : BaseComposeActivity() {
    @Inject
    lateinit var detailMemoNavigator: DetailMemoNavigator

    @Inject
    lateinit var galleryNavigator: GalleryNavigator

    private val viewModel by viewModels<MemoViewModel>()

    @Composable
    override fun Screen() {
        FishingMemoryTheme {
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.let { intent ->
                        handleActivityResult(intent)
                    }
                }
            }
            setMemo(intent)
            MemoCreateRoute(
                viewModel = viewModel,
                onBack = { finish() },
                navigateToMemoDetail = { memoUi ->
                    val intent = detailMemoNavigator.intent(this).apply {
                        putExtra(EXTRA_REQUEST_CODE, REQUEST_UPDATE_MEMO)
                        putExtra(MEMO_DATA, memoUi)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    }
                    launcher.launch(intent)
                    finish()
                },
                navigateToGallery = {
                    val intent = galleryNavigator.intent(this).apply {
                        type = "image/*"
                        putExtra(EXTRA_REQUEST_CODE, REQUEST_IMAGE_CAPTURE)
                    }
                    launcher.launch(intent)
                },
                navigateToLocationSetting = {
                    val intent = Intent(this, LocationSettingActivity::class.java).apply {
                        putExtra(EXTRA_REQUEST_CODE, REQUEST_CODE_AREA)
                    }
                    launcher.launch(intent)
                },
            )
        }
    }

    private fun setMemo(intent: Intent) {
        val memo = intent.getParcelableExtraData(MEMO_DATA, MemoUI::class.java) ?: MemoUI(
            date = SimpleDateFormat(
                "yyyy/MM/dd",
                Locale.KOREA
            ).format(Date())
        )
        if (memo.uuid.isNotEmpty()) {
            viewModel.setMemoUi(memoUI = memo)
        }
    }

    private fun handleActivityResult(intent: Intent) {
        when (intent.getIntExtra(EXTRA_REQUEST_CODE, -1)) {
            REQUEST_CODE_AREA -> handleAreaResult(intent)
            REQUEST_IMAGE_CAPTURE -> handleImageCaptureResult(intent)
            REQUEST_UPDATE_MEMO -> handleUpdateMemoResult(intent)
        }
    }

    private fun handleAreaResult(intent: Intent) {
        val location = intent.getStringExtra(ARG_AREA) ?: String.Empty
        val coords = intent.getStringExtra(ARG_AREA_COORDS) ?: String.Empty
        viewModel.setCoords(coords)
        viewModel.setLocation(location)
    }

    private fun handleImageCaptureResult(intent: Intent) {
        val uri = intent.getParcelableExtraData(PHOTO_FILE, Uri::class.java)?.path ?: String.Empty
        viewModel.setImage(uri)
    }

    private fun handleUpdateMemoResult(intent: Intent) {
        setMemo(intent)
    }
}
