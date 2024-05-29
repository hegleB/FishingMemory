package com.qure.memo.detail

import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.qure.core.BaseComposeActivity
import com.qure.core.extensions.Empty
import com.qure.core_design.compose.theme.FishingMemoryTheme
import com.qure.domain.MEMO_DATA
import com.qure.domain.UPDATE_MEMO
import com.qure.memo.model.MemoUI
import com.qure.navigator.MemoCreateNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailMemoActivity : BaseComposeActivity() {
    @Inject
    lateinit var memoCreateNavigator: MemoCreateNavigator

    private val viewModel by viewModels<DetailMemoViewModel>()
    private var memo: MemoUI = MemoUI()

    @Composable
    override fun Screen() {
        initData()
        FishingMemoryTheme {
            DetailMemoScreen(
                memo = memo,
                viewModel = viewModel,
                onBack = { finish() },
                onClickEdit = {
                    val intent = memoCreateNavigator.intent(this).apply {
                        putExtra(EXTRA_REQUEST_CODE, REQUEST_UPDATE_MEMO)
                        putExtra(MEMO_DATA, memo)
                        setResult(RESULT_OK)
                    }
                    startActivity(intent)
                },
            )
        }
    }

    private fun initData() {
        memo =
            when {
                intent.getParcelableExtra<MemoUI>(UPDATE_MEMO) != null ->
                    intent.getParcelableExtra(UPDATE_MEMO) ?: MemoUI()

                Intent.ACTION_VIEW == intent.action -> {
                    val uri = intent.data
//                    binding.imageViewActivityDetailMemoMore.gone()
                    uri?.let { createMemoUI(it) } ?: MemoUI()
                }

                else -> intent.getParcelableExtra(MEMO_DATA) ?: MemoUI()
            }
    }

    //
    fun createMemoUI(uri: Uri): MemoUI {
        return MemoUI(
            title = uri.getQueryParameter(QUERY_TITLE) ?: String.Empty,
            waterType = uri.getQueryParameter(QUERY_WATER_TYPE) ?: String.Empty,
            fishSize = uri.getQueryParameter(QUERY_FISH_SIZE) ?: String.Empty,
            fishType = uri.getQueryParameter(QUERY_FISH_TYPE) ?: String.Empty,
            date = uri.getQueryParameter(QUERY_CREATE_TIME) ?: String.Empty,
            location = uri.getQueryParameter(QUERY_LOCATION) ?: String.Empty,
            content = uri.getQueryParameter(QUERY_CONTENT) ?: String.Empty,
            image =
            uri.getQueryParameter(QUERY_BASE_URL) + SLASH +
                uri.getQueryParameter(QUERY_IMAGE_PATH),
        )
    }

    companion object {
        const val SIZE_UNIT = "CM"
        const val QUERY_TITLE = "title"
        const val QUERY_WATER_TYPE = "waterType"
        const val QUERY_FISH_SIZE = "fishSize"
        const val QUERY_FISH_TYPE = "fishType"
        const val QUERY_CREATE_TIME = "createTime"
        const val QUERY_LOCATION = "location"
        const val QUERY_CONTENT = "content"
        const val QUERY_BASE_URL = "baseUrl"
        const val QUERY_IMAGE_PATH = "imagePath"
        const val SLASH = "%2F"
    }
}
