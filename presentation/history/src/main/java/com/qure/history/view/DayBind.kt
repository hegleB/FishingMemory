package com.qure.history.view

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.qure.core.extensions.getColorCompat
import com.qure.core.extensions.getDrawableCompat
import com.qure.history.R
import com.qure.history.databinding.CalendarDayBinding
import timber.log.Timber
import java.time.LocalDate

class DayBind: MonthDayBinder<DayContainer> {

    private val today = LocalDate.now()

    override fun bind(container: DayContainer, data: CalendarDay) {
        val context = container.view.context
        val textView = container.textView
        val roundBackgroundView = container.round
        val todayBackground = context.getDrawableCompat(R.drawable.bg_today)

        container.textView.text = data.date.dayOfMonth.toString()

        if (data.position == DayPosition.MonthDate) {
            textView.setTextColor(context.getColor(android.R.color.black))
            if (data.date == today) {
                roundBackgroundView.applyBackground(todayBackground)
                textView.setTextColor(context.getColorCompat(R.color.white))
            }
        } else {
            textView.setTextColor(context.getColorCompat(com.qure.core_design.R.color.gray_300))
        }
    }

    override fun create(view: View): DayContainer {
        return DayContainer(view)
    }

    private fun View.applyBackground(drawable: Drawable) {
        visibility = View.VISIBLE
        background = drawable
    }

    companion object {
        fun newInstance(): DayBind =
            DayBind()
    }
}


class DayContainer(view: View) : ViewContainer(view) {
    private val binding = CalendarDayBinding.bind(view)
    val textView = binding.textViewDay
    val round = binding.viewRound
}
