package com.qure.home.home

import androidx.compose.runtime.Composable
import androidx.fragment.app.activityViewModels
import com.qure.core.BaseComposeFragment
import com.qure.core_design.compose.theme.FishingMemoryTheme
import com.qure.domain.MEMO_DATA
import com.qure.navigator.DetailMemoNavigator
import com.qure.navigator.MapNavigator
import com.qure.navigator.MemoNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseComposeFragment() {
    @Inject
    lateinit var memoNavigator: MemoNavigator

    @Inject
    lateinit var detailMemoNavigator: DetailMemoNavigator

    @Inject
    lateinit var mapNavigator: MapNavigator

    private val viewModel by activityViewModels<HomeViewModel>()

    @Composable
    override fun Screen() {
        FishingMemoryTheme {
            HomeRoute(
                viewModel = viewModel,
                navigateToMemoList = {
                    startActivity(memoNavigator.intent(requireContext()))
                },
                navigateToDetailMemo = { memo ->
                    val intent = detailMemoNavigator.intent(requireContext())
                    intent.putExtra(MEMO_DATA, memo)
                    startActivity(intent)
                },
                navigateToMap = {
                    startActivity(mapNavigator.intent(requireContext()))
                }
            )
        }
    }
}
