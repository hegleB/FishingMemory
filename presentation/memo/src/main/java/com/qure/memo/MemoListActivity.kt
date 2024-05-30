package com.qure.memo

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.qure.core.BaseComposeActivity
import com.qure.core_design.compose.theme.FishingMemoryTheme
import com.qure.domain.MEMO_DATA
import com.qure.navigator.DetailMemoNavigator
import com.qure.navigator.MemoCreateNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MemoListActivity : BaseComposeActivity() {
    @Inject
    lateinit var detailMemoNavigator: DetailMemoNavigator

    @Inject
    lateinit var memoCreateNavigator: MemoCreateNavigator

    private val viewModel by viewModels<MemoListViewModel>()

    @Composable
    override fun Screen() {
        FishingMemoryTheme {
            MemoListRoute(
                viewModel = viewModel,
                onBack = { finish() },
                navigateToMemoCreate = { startActivity(memoCreateNavigator.intent(this)) },
                navigateToMemoDetail = { memo ->
                    val intent = detailMemoNavigator.intent(this)
                    intent.putExtra(MEMO_DATA, memo)
                    startActivity(intent)
                },
            )
        }
    }
}
