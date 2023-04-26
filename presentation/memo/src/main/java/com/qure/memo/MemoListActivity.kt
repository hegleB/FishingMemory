package com.qure.memo

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.qure.core.BaseActivity
import com.qure.core.extensions.gone
import com.qure.core.extensions.initSwipeRefreshLayout
import com.qure.core.extensions.visiable
import com.qure.core.util.FishingMemoryToast
import com.qure.core.util.setOnSingleClickListener
import com.qure.memo.databinding.ActivityMemoListBinding
import com.qure.memo.detail.DetailMemoActivity.Companion.MEMO_DATA
import com.qure.navigator.DetailMemoNavigator
import com.qure.navigator.MemoCreateNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MemoListActivity : BaseActivity<ActivityMemoListBinding>(R.layout.activity_memo_list) {

    @Inject
    lateinit var detailMemoNavigator: DetailMemoNavigator

    @Inject
    lateinit var memoCreateNavigator: MemoCreateNavigator

    private val viewModel by viewModels<MemoListViewModel>()

    private val adapter: MemoListAdapter by lazy {
        MemoListAdapter(
            onMemoClick = { memo ->
                val intent = detailMemoNavigator.intent(this)
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
        binding.swipeRefreshLayoutActivityMemoList.initSwipeRefreshLayout()
        binding.swipeRefreshLayoutActivityMemoList.setOnRefreshListener {
            refreshMemoList()
        }
        binding.swipeRefreshLayoutActivityMemoList.setRefreshing(false)

        binding.imageViewActivityMemoListBack.setOnSingleClickListener {
            finish()
        }

        binding.lottieAnimationActivityMemoListFishing.apply {
            setAnimation(R.raw.fishing)
        }

        binding.imageViewActivityMemoListAdd.setOnSingleClickListener {
            startActivity(memoCreateNavigator.intent(this))
        }
    }

    private fun refreshMemoList() = lifecycleScope.launch {
        viewModel.getFilteredMemo()
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                delay(500)
                viewModel.uiState.collect { uiState ->
                    if (uiState.isFilterInitialized) {
                        binding.swipeRefreshLayoutActivityMemoList.setRefreshing(false)
                        adapter.submitList(uiState.filteredMemo) {
                            binding.recyclerViewActivityMemoList.scrollToPosition(0)
                        }
                    }
                }
            }
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
                        handleUiState(it)
                    }
                }
            }
        }
    }

    private fun handleUiState(uidState: UiState) {
        if (uidState.isFilterInitialized) {
            setViewVisiable(uidState)
            binding.progressBarActivityMemoList.gone()
        } else {
            binding.progressBarActivityMemoList.visiable()
        }
    }

    private fun setViewVisiable(uidState: UiState) {
        if (uidState.filteredMemo.isEmpty()) {
            binding.groupActivityMemoListEmpty.visiable()
            binding.recyclerViewActivityMemoList.gone()
        } else {
            binding.groupActivityMemoListEmpty.gone()
            binding.recyclerViewActivityMemoList.visiable()
            adapter.submitList(uidState.filteredMemo)
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewActivityMemoList.adapter = adapter
    }
}