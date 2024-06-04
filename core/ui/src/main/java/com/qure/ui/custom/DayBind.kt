package com.qure.ui.custom

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.qure.core.ui.R
import com.qure.ui.model.MemoUI
import java.time.LocalDate

class DayBind(
    private val calendarView: CalendarView,
    private val memos: List<MemoUI> = emptyList(),
) : MonthDayBinder<DayBind.DayContainer> {
    private val today = LocalDate.now()
    private var calendar: LocalDate? = null

    var input: Input? = null

    fun updateCalendar(calendar: LocalDate) {
        if (this.calendar == calendar) return
        this.calendar = calendar
        this.calendarView.notifyCalendarChanged()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun bind(
        container: DayContainer,
        data: CalendarDay,
    ) {
        val context = container.view.context
        val textView = container.textViewDay
        val roundBackgroundView = container.viewRound
        val dot = container.imageViewDot
        val todayBackground = context.getDrawable(R.drawable.bg_today)
        val selectedBackgroud = context.getDrawable(R.drawable.bg_selected)

        val selectedDay = this.calendar

        container.view.setOnClickListener { _ ->
            input?.onDayClick(data.date)
        }

        textView.text = data.date.dayOfMonth.toString()

        if (memos.isNotEmpty()) {
            val hasMemo =
                memos.any {
                    it.date == data.date.toString().split("-").joinToString("/")
                }
            dot.visibility = if (hasMemo) View.VISIBLE else View.INVISIBLE
        }

        if (data.position == DayPosition.MonthDate) {
            textView.setTextColor(context.getColor(R.color.text_day_color))
            when (data.date) {
                today -> {
                    if (todayBackground != null) {
                        roundBackgroundView.applyBackground(todayBackground)
                    }
                    textView.setTextColor(context.getColor(R.color.white))
                }
                selectedDay -> {
                    if (selectedBackgroud != null) {
                        roundBackgroundView.applyBackground(selectedBackgroud)
                    }
                }
                else -> {
                    roundBackgroundView.background = null
                    textView.setTextColor(context.getColor(R.color.text_day_color))
                }
            }
        } else {
            textView.setTextColor(context.getColor(R.color.gray_300))
        }
    }

    override fun create(view: View): DayContainer {
        return DayContainer(view)
    }

    private fun View.applyBackground(drawable: Drawable) {
        visibility = View.VISIBLE
        background = drawable
    }

    class DayContainer(view: View) : ViewContainer(view) {
        val textViewDay: TextView = view.findViewById(R.id.textView_day)
        val viewRound: View = view.findViewById(R.id.view_round)
        val imageViewDot: ImageView = view.findViewById(R.id.imageView_dot)
    }

    abstract class Input {
        abstract fun onDayClick(date: LocalDate)
    }
}