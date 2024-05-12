package com.qure.history

import androidx.fragment.app.activityViewModels
import com.qure.domain.MEMO_DATA
import com.qure.history.databinding.FragmentHistoryBinding
import com.qure.navigator.DetailMemoNavigator
import com.qure.navigator.MapNavigator
import com.qure.navigator.MemoCreateNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>(R.layout.fragment_history) {
    @Inject
    lateinit var memoCreateNavigator: MemoCreateNavigator

    @Inject
    lateinit var detailMemoNavigator: DetailMemoNavigator

    @Inject
    lateinit var mapNavigator: MapNavigator

    private val viewModel by activityViewModels<HistoryViewModel>()
                },
            )
        }
    }
}
