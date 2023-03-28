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
class HistoryFragment : BaseFragment<FragmentHistoryBinding>(R.layout.fragment_history),
    YearPickerDialogFragment.YearDialogListner {

    private lateinit var firstMonth: YearMonth
    private lateinit var firstDayOfWeek: DayOfWeek
    private lateinit var binder: DayBind
    private var currentYear: Int = 0
    private var currentMonth: Int = 0
    lateinit var listener: YearPickerDialogFragment.YearDialogListner

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener = this

        initCalendar()
        changeCalendarMonth()
        setCalendarYear()
    }

    private fun initCalendar() {
        firstMonth = YearMonth.of(LocalDate.now().year, LocalDate.now().month)
        currentYear = LocalDate.now().year
        firstDayOfWeek = WeekFields.of(Locale.KOREAN).firstDayOfWeek
        binder = DayBind(binding.calendarViewFragmentHistory).apply {
            input = object : DayBind.Input() {
                override fun onDayClick(date: LocalDate) = dayClick(date)
            }
        }
        with(binding) {
            textViewFragmentHistoryYear.text = LocalDate.now().year.toString()
            calendarViewFragmentHistory.setup(firstMonth, firstMonth, firstDayOfWeek)
            calendarViewFragmentHistory.dayBinder = binder
            tabLayoutFragmentHistoryMonth.tabs.post({
                tabLayoutFragmentHistoryMonth.tabs.getTabAt(firstMonth.monthValue - 1)?.select()
            })
        }
    }

    private fun setCalendarYear() {
        binding.textViewFragmentHistoryYear.setOnClickListener {
            YearPickerDialogFragment.newInstance(
                year = currentYear,
                listener = listener
            ).show(requireActivity().supportFragmentManager, YearPickerDialogFragment.TAG)
        }
    }

    private fun changeCalendarMonth() {
        with(binding) {
            tabLayoutFragmentHistoryMonth.tabs.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val month =  tab?.position?.plus(1)!!
                    setupCalendarView(currentYear, month)
                    currentMonth = month
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
                override fun onTabReselected(tab: TabLayout.Tab?) = Unit
            })
        }
    }

    private fun dayClick(date: LocalDate) {
        binder.updateCalendar(date)
    }

    private fun setupCalendarView(year: Int, month: Int) {
        firstMonth = YearMonth.of(year, month)
        binding.calendarViewFragmentHistory.setup(firstMonth, firstMonth, firstDayOfWeek)
    }

    override fun selectYearClick(year: Int) {
        this.currentYear = year
        setupCalendarView(year, currentMonth)
        with(binding) {
            textViewFragmentHistoryYear.text = year.toString()
            tabLayoutFragmentHistoryMonth.tabs.post({
                tabLayoutFragmentHistoryMonth.tabs.getTabAt(firstMonth.monthValue - 1)?.select()
            })
        }
    }
}
