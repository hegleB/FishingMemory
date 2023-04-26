package com.qure.history.view

import android.graphics.drawable.Drawable
import android.view.View
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.qure.core.extensions.Dash
import com.qure.core.extensions.Slash
import com.qure.core.extensions.getColorCompat
import com.qure.core.extensions.getDrawableCompat
import com.qure.core.util.setOnSingleClickListener
import com.qure.history.R
import com.qure.history.databinding.CalendarDayBinding
import com.qure.memo.model.MemoUI
import java.time.LocalDate

class DayBind(
    private val calendarView: CalendarView,
    private val memos: List<MemoUI> = emptyList()
) : MonthDayBinder<DayBind.DayContainer> {

    private val today = LocalDate.now()
    private var calendar: LocalDate? = null

    var input: Input? = null

    fun updateCalendar(calendar: LocalDate) {
        if (this.calendar == calendar) return
        this.calendar = calendar
        this.calendarView.notifyCalendarChanged()
    }

    override fun bind(container: DayContainer, data: CalendarDay) {
        val context = container.binding.root.context
        val textView = container.binding.textViewDay
        val roundBackgroundView = container.binding.viewRound
        val dot = container.binding.imageViewDot
        val todayBackground = context.getDrawableCompat(R.drawable.bg_today)
        val selectedBackgroud = context.getDrawableCompat(R.drawable.bg_selected)

        val selectedDay = this.calendar

        container.binding.root.setOnSingleClickListener { _ ->
            input?.onDayClick(data.date)
        }

        textView.text = data.date.dayOfMonth.toString()

        if (memos.isNotEmpty()) {
            val hasMemo = memos.any {
                it.date == data.date.toString().split(String.Dash).joinToString(String.Slash)
            }
            dot.visibility = if (hasMemo) View.VISIBLE else View.INVISIBLE
        }

        if (data.position == DayPosition.MonthDate) {
            textView.setTextColor(context.getColor(android.R.color.black))
            when (data.date) {
                today -> {
                    roundBackgroundView.applyBackground(todayBackground)
                    textView.setTextColor(context.getColorCompat(R.color.white))
                }
                selectedDay -> {
                    roundBackgroundView.applyBackground(selectedBackgroud)
                }
                else -> {
                    roundBackgroundView.background = null
                    textView.setTextColor(context.getColorCompat(R.color.black))
                }
            }

        } else {
            textView.setTextColor(context.getColorCompat(com.qure.core_design.R.color.gray_300))
        }
    }

    override fun create(view: View): DayContainer {
        val binding = CalendarDayBinding.bind(view)
        return DayContainer(binding)
    }

    private fun View.applyBackground(drawable: Drawable) {
        visibility = View.VISIBLE
        background = drawable
    }

    class DayContainer(val binding: CalendarDayBinding) : ViewContainer(binding.root)

    abstract class Input {
        abstract fun onDayClick(date: LocalDate)
    }
}