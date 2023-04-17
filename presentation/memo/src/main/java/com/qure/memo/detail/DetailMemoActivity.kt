package com.qure.memo.detail

import android.os.Bundle
import com.bumptech.glide.Glide
import com.qure.core.BaseActivity
import com.qure.core.util.setOnSingleClickListener
import com.qure.memo.R
import com.qure.memo.databinding.ActivityDetailMemoBinding
import com.qure.memo.model.MemoUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailMemoActivity : BaseActivity<ActivityDetailMemoBinding>(R.layout.activity_detail_memo) {

    private var memo: MemoUI = MemoUI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initView()
    }

    private fun initData() {
        memo = intent.getParcelableExtra(MEMO_DATA) ?: MemoUI()
    }

    private fun initView() {
        binding.apply {
            setMemoView()

            imageViewActivityDetailMemoBack.setOnSingleClickListener {
                finish()
            }
        }

    }

    private fun ActivityDetailMemoBinding.setMemoView() {
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

    companion object {
        const val MEMO_DATA = "memoData"
        private const val SIZE_UNIT = "CM"
    }
}