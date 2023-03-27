package com.qure.history


import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.qure.core.BaseFragment
import com.qure.history.databinding.FragmentHistoryBinding
import com.qure.history.view.DayBind
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>(R.layout.fragment_history) {

    private lateinit var firstMonth: YearMonth
    private lateinit var firstDayOfWeek: DayOfWeek

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firstMonth = YearMonth.of(LocalDate.now().year, LocalDate.now().month)
        firstDayOfWeek = WeekFields.of(Locale.KOREAN).firstDayOfWeek

        initCalendar(firstMonth)
        changeCalendarMonth()
    }

    private fun initCalendar(firstMonth: YearMonth) {

        with(binding) {
            tabLayoutFragmentHistoryMonth.tabs.post({
                tabLayoutFragmentHistoryMonth.tabs.getTabAt(firstMonth.monthValue - 1)?.select()
            })
            textViewFragmentHistoryYear.text = LocalDate.now().year.toString()
            calendarViewFragmentHistory.dayBinder = DayBind.newInstance()
            calendarViewFragmentHistory.setup(firstMonth, firstMonth, firstDayOfWeek)

        }
    }

    private fun changeCalendarMonth() {
        with(binding) {
            tabLayoutFragmentHistoryMonth.tabs.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    calendarViewFragmentHistory.monthScrollListener = { calendarMonth ->
                        firstMonth = YearMonth.of(LocalDate.now().year, tab?.position?.plus(1)!!)
                        calendarViewFragmentHistory.setup(firstMonth, firstMonth, firstDayOfWeek)
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
                override fun onTabReselected(tab: TabLayout.Tab?) = Unit
            })
        }
    }
}
