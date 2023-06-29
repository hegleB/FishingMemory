package com.qure.fishingspot.bookmark

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.qure.core.BaseActivity
import com.qure.core.extensions.gone
import com.qure.core.extensions.visiable
import com.qure.core.util.FishingMemoryToast
import com.qure.core.util.setOnSingleClickListener
import com.qure.domain.SPOT_DATA
import com.qure.fishingspot.R
import com.qure.fishingspot.databinding.ActivityBookmarkBinding
import com.qure.memo.delete.DeleteDialogFragment
import com.qure.navigator.FishingSpotNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class BookmarkActivity : BaseActivity<ActivityBookmarkBinding>(R.layout.activity_bookmark), OnDeleteClickListener {

    @Inject
    lateinit var fishingSpotNavigator: FishingSpotNavigator

    private val viewModel by viewModels<BookmarkViewModel>()
    private val adapter: BookmarkAdatper by lazy {
        BookmarkAdatper({
            val intent = fishingSpotNavigator.intent(this)
            intent.putExtra(SPOT_DATA, it)
            startActivity(intent)
        }, {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:${it}"))
            startActivity(intent)
        })

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecyclerView()
        initView()
        observe()
    }
    private fun initView() {
        binding.imageViewActivityBookmarkBack.setOnSingleClickListener {
            finish()
        }

        binding.textViewActivityBookmarkDeleteAll.setOnSingleClickListener {
            onDeleteAllClicked()
        }
    }
    private fun onDeleteAllClicked() {
        BookmarkDeleteDialogFragment()
            .show(
                this.supportFragmentManager,
                BookmarkDeleteDialogFragment.TAG,
            )
    }
    private fun initRecyclerView() {
        binding.recyclerViewActivityBookmark.adapter = adapter
    }
    private fun observe() {
        viewModel.error
            .onEach { errorMessage -> FishingMemoryToast().error(this, errorMessage) }
            .launchIn(lifecycleScope)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.fishingSpotBookmarks.collect {
                        if (it.isEmpty()) {
                            binding.textViewActivityBookmarkEmpty.visiable()
                            binding.recyclerViewActivityBookmark.gone()
                        } else {
                            binding.textViewActivityBookmarkEmpty.gone()
                            binding.recyclerViewActivityBookmark.visiable()
                            adapter.submitList(it)
                        }
                    }
                }

                launch {
                    viewModel.isLoading.collect {
                        if (it) {
                            binding.progressBarActivityBookmark.visiable()
                        } else {
                            binding.progressBarActivityBookmark.gone()
                        }
                    }
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        viewModel.getBookmarks()
    }
    override fun onDeleteClicked() {
        viewModel.deleteAllBookmarks()
    }
}