package com.qure.history

import androidx.compose.runtime.Composable
import androidx.fragment.app.activityViewModels
import com.qure.core.BaseComposeFragment
import com.qure.core_design.compose.theme.FishingMemoryTheme
import com.qure.domain.MEMO_DATA
import com.qure.navigator.DetailMemoNavigator
import com.qure.navigator.MapNavigator
import com.qure.navigator.MemoCreateNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment : BaseComposeFragment() {
    @Inject
    lateinit var memoCreateNavigator: MemoCreateNavigator

    @Inject
    lateinit var detailMemoNavigator: DetailMemoNavigator

    @Inject
    lateinit var mapNavigator: MapNavigator

    private val viewModel by activityViewModels<HistoryViewModel>()

    @Composable
    override fun Screen() {
        FishingMemoryTheme {
            HistoryRoute(
                viewModel = viewModel,
                navigateToMap = { startActivity(mapNavigator.intent(requireContext())) },
                navigateToMemoCreate = { startActivity(memoCreateNavigator.intent(requireContext())) },
                navigateToMemoDetail = { memo ->
                    val intent = detailMemoNavigator.intent(requireContext())
                    intent.putExtra(MEMO_DATA, memo)
                    startActivity(intent)
                },
                onSelectedMonthChange = viewModel::selectMonth,
                onSelectedDayChange = viewModel::selectDate,
                onSelectedYearChange = viewModel::selectYear,
            )
        }
    }
}
