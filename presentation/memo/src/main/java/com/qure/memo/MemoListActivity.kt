package com.qure.memo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.qure.core.BaseActivity
import com.qure.core.util.FishingMemoryToast
import com.qure.core.util.setOnSingleClickListener
import com.qure.memo.databinding.ActivityMemoListBinding
import com.qure.memo.detail.DetailMemoActivity
import com.qure.memo.detail.DetailMemoActivity.Companion.MEMO_DATA
import com.qure.memo.model.MemoUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MemoListActivity : BaseActivity<ActivityMemoListBinding>(R.layout.activity_memo_list) {

    private val viewModel by viewModels<MemoListViewModel>()

    private val adapter: MemoListAdapter by lazy {
        MemoListAdapter(
            onMemoClick = { memo ->
                val intent = Intent(this@MemoListActivity, DetailMemoActivity::class.java)
                intent.putExtra(MEMO_DATA, memo)
                startActivity(intent)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getFilteredMemo()
        observe()
        initView()
        initRecyclerView()
    }

    private fun initView() {
        binding.imageViewActivityMemoListBack.setOnSingleClickListener {
            finish()
        }
    }

    private fun observe() {
        viewModel.error
            .onEach { errorMessage -> FishingMemoryToast().error(this, errorMessage) }
            .launchIn(lifecycleScope)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect {
                        adapter.submitList(it.filteredMemo)
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewActiviryMemoList.adapter = adapter
    }
}