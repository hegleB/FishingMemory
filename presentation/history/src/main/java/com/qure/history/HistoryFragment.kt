package com.qure.history

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.tabs.TabLayout
import com.qure.core.BaseFragment
import com.qure.core.extensions.gone
import com.qure.core.extensions.initSwipeRefreshLayout
import com.qure.core.extensions.visiable
import com.qure.core.util.FishingMemoryToast
import com.qure.core.util.setOnSingleClickListener
import com.qure.domain.MEMO_DATA
import com.qure.history.databinding.FragmentHistoryBinding
import com.qure.history.view.DayBind
import com.qure.navigator.DetailMemoNavigator
import com.qure.navigator.MapNavigator
import com.qure.navigator.MemoCreateNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*
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

    private val adapter: HistoryAdapter by lazy {
        HistoryAdapter(
            onMemoClick = {
                val intent = detailMemoNavigator.intent(requireContext())
                intent.putExtra(MEMO_DATA, it)
                startActivity(intent)
            },
        )
    }

    private lateinit var firstMonth: YearMonth
    private lateinit var firstDayOfWeek: DayOfWeek
    private lateinit var binder: DayBind
    private var currentYear: Int = 0
    private var currentMonth: Int = LocalDate.now().monthValue

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initCalendar()
        initView()
        observe()
        changeCalendarMonth()
        setCalendarYear()
        moveMemoCreate()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getFilteredDayMemo(LocalDate.now())
        viewModel.getFilteredMemo()
    }

    private fun initView() {
        with(binding) {
            swipeRefreshLayoutFragmentHistory.initSwipeRefreshLayout()
            swipeRefreshLayoutFragmentHistory.setOnRefreshListener {
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        delay(500)
                        viewModel.selectedDate.collect {
                            viewModel.getFilteredDayMemo(it)
                            viewModel.getFilteredMemo()
                            swipeRefreshLayoutFragmentHistory.setRefreshing(false)
                        }
                    }
                }
            }
            imageViewFragmentHistoryMap.setOnSingleClickListener {
                startActivity(mapNavigator.intent(requireContext()))
            }
        }
    }

    private fun observe() {
        viewModel.error
            .onEach { errorMessage -> FishingMemoryToast().error(requireContext(), errorMessage) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.selectedDayMemos.collect {
                        adapter.submitList(it) {
                            binding.recyclerViewFragmentHistoryPost.scrollToPosition(0)
                        }
                        binding.progressBarFragmentHistory.gone()
                    }
                }

                launch {
                    viewModel.uiState.collect {
                        if (it.isFiltered) {
                            binder =
                                DayBind(
                                    binding.calendarViewFragmentHistory,
                                    it.filteredMemos,
                                ).apply {
                                    input =
                                        object : DayBind.Input() {
                                            override fun onDayClick(date: LocalDate) = dayClick(date)
                                        }
                                }
                            binding.calendarViewFragmentHistory.dayBinder = binder
                            binding.progressBarFragmentHistory.gone()
                        } else {
                            binding.progressBarFragmentHistory.visiable()
                        }
                    }
                }

                launch {
                    viewModel.selectedYearEvent.collect { year ->
                        viewModel.selectYear(year)
                    }
                }

                launch {
                    viewModel.selectedYear.collect { year ->
                        setYearCalendar(year ?: LocalDate.now().year)
                    }
                }

                launch {
                    viewModel.selectedMonth.collect { month ->
                        setupCalendarView(
                            viewModel.selectedYear.value ?: LocalDate.now().year,
                            month ?: LocalDate.now().monthValue,
                        )
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewFragmentHistoryPost.adapter = adapter
    }

    private fun initCalendar() {
        firstMonth = YearMonth.of(LocalDate.now().year, LocalDate.now().month)
        firstDayOfWeek = WeekFields.of(Locale.KOREAN).firstDayOfWeek
        binder = DayBind(binding.calendarViewFragmentHistory)
        with(binding) {
            calendarViewFragmentHistory.setup(firstMonth, firstMonth, firstDayOfWeek)
            calendarViewFragmentHistory.dayBinder = binder
            Handler().post({
                tabLayoutFragmentHistoryMonth.tabs.post({
                    tabLayoutFragmentHistoryMonth.tabs.getTabAt(firstMonth.monthValue - 1)?.select()
                })
            })
        }
    }

    private fun setCalendarYear() {
        binding.textViewFragmentHistoryYear.setOnClickListener {
            YearPickerDialogFragment.newInstance(
                year = currentYear,
            ).show(requireActivity().supportFragmentManager, YearPickerDialogFragment.TAG)
        }
    }

    private fun changeCalendarMonth() {
        with(binding) {
            tabLayoutFragmentHistoryMonth.tabs.addOnTabSelectedListener(
                object :
                    TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        val month = tab?.position?.plus(1)!!
                        viewModel.selectMonth(month)
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

                    override fun onTabReselected(tab: TabLayout.Tab?) = Unit
                },
            )
        }
    }

    private fun dayClick(date: LocalDate) {
        viewModel.getFilteredDayMemo(date)
        viewModel.selectDate(date)
        binder.updateCalendar(date)
    }

    private fun setupCalendarView(
        year: Int,
        month: Int,
    ) {
        firstMonth = YearMonth.of(year, month)
        binding.calendarViewFragmentHistory.setup(firstMonth, firstMonth, firstDayOfWeek)
    }

    private fun moveMemoCreate() {
        binding.floatingActionButtonFragmentHistoryPostWriting.setOnClickListener {
            startActivity(memoCreateNavigator.intent(requireContext()))
        }
    }

    private fun setYearCalendar(year: Int) {
        setupCalendarView(year, currentMonth)
        viewModel.getFilteredDayMemo(
            LocalDate.of(
                year,
                currentMonth,
                LocalDate.now().dayOfMonth,
            ),
        )
        viewModel.getFilteredMemo()
        with(binding) {
            textViewFragmentHistoryYear.text = year.toString()
            Handler().post {
                (
                    tabLayoutFragmentHistoryMonth.tabs
                        .post({
                            tabLayoutFragmentHistoryMonth.tabs
                                .getTabAt(firstMonth.monthValue - 1)
                                ?.select()
                        })
                )
            }
        }
    }
}
