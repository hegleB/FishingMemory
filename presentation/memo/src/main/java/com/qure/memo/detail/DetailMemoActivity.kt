package com.qure.memo.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.qure.core.BaseActivity
import com.qure.core.extensions.Empty
import com.qure.core.extensions.gone
import com.qure.core.util.FishingMemoryToast
import com.qure.core.util.setOnSingleClickListener
import com.qure.domain.MEMO_DATA
import com.qure.domain.UPDATE_MEMO
import com.qure.memo.R
import com.qure.memo.databinding.ActivityDetailMemoBinding
import com.qure.memo.delete.DeleteDialogFragment
import com.qure.memo.model.MemoUI
import com.qure.memo.share.ShareDialogFragment
import com.qure.navigator.MemoCreateNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class DetailMemoActivity : BaseActivity<ActivityDetailMemoBinding>(R.layout.activity_detail_memo) {

    @Inject
    lateinit var memoCreateNavigator: MemoCreateNavigator

    private val viewModel by viewModels<DetailMemoViewModel>()
    private var memo: MemoUI = MemoUI()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initView()
        observe()
    }

    private fun observe() {
        viewModel.error
            .onEach { errorMessage -> FishingMemoryToast().error(this, errorMessage) }
            .launchIn(lifecycleScope)
    }

    private fun initData() {
        memo = when {
            Intent.ACTION_VIEW == intent.action -> {
                val uri = intent.data
                binding.imageViewActivityDetailMemoMore.gone()
                uri?.let { createMemoUI(it) } ?: MemoUI()
            }
            intent.getParcelableExtra<MemoUI>(UPDATE_MEMO) != null ->
                intent.getParcelableExtra(UPDATE_MEMO) ?: MemoUI()
            else -> intent.getParcelableExtra(MEMO_DATA) ?: MemoUI()
        }
    }

    fun createMemoUI(uri: Uri): MemoUI {
        return MemoUI(
            title = uri.getQueryParameter(QUERY_TITLE) ?: String.Empty,
            waterType = uri.getQueryParameter(QUERY_WATER_TYPE) ?: String.Empty,
            fishSize = uri.getQueryParameter(QUERY_FISH_SIZE) ?: String.Empty,
            fishType = uri.getQueryParameter(QUERY_FISH_TYPE) ?: String.Empty,
            date = uri.getQueryParameter(QUERY_CREATE_TIME) ?: String.Empty,
            location = uri.getQueryParameter(QUERY_LOCATION) ?: String.Empty,
            content = uri.getQueryParameter(QUERY_CONTENT) ?: String.Empty,
            image = uri.getQueryParameter(QUERY_BASE_URL) + SLASH
                    + uri.getQueryParameter(QUERY_IMAGE_PATH),
        )
    }


    private fun initView() {
        setMemoView()
        binding.apply {
            imageViewActivityDetailMemoBack.setOnSingleClickListener {
                finish()
            }

            imageViewActivityDetailMemoMore.setOnSingleClickListener {
                showPopUp(it)
            }
        }

    }

    private fun showPopUp(view: View) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.menu_detail_memo)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_share -> {
                    ShareDialogFragment.newInstance(memo)
                        .show(
                            this.supportFragmentManager,
                            ShareDialogFragment.TAG
                        )
                    true
                }
                R.id.menu_delete -> {
                    DeleteDialogFragment.newInstance(memo.uuid)
                        .show(
                            this.supportFragmentManager,
                            DeleteDialogFragment.TAG
                        )
                    true
                }
                R.id.menu_edit -> {
                    val intent = memoCreateNavigator.intent(this)
                    intent.putExtra(UPDATE_MEMO, memo)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }


    private fun setMemoView() {
        binding.apply {
            textViewActivityDetailMemoFishNameHeadline.text = memo.fishType
            textViewActivityDetailMemoFishName.text = memo.fishType
            textViewActivityDetailMemoWaterType.text = memo.waterType
            textViewActivityDetailMemoFishSize.text = memo.fishSize + SIZE_UNIT
            textViewActivityDetailMemoContent.text = memo.content
            textViewActivityDetailMemoLocation.text = memo.location
            textViewActivityDetailMemoCreateTime.text = memo.date
            textViewActivityDetailMemoTitle.text = memo.title

            Glide.with(this@DetailMemoActivity)
                .load(memo.image)
                .into(imageViewActivityDetailMemoFishImage)
        }
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