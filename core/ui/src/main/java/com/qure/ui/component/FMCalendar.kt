package com.qure.ui.component

import android.annotation.SuppressLint
import android.widget.LinearLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.view.CalendarView
import com.qure.ui.custom.DayBind
import com.qure.ui.model.MemoUI
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale
import com.qure.core.ui.R

@SuppressLint("RememberReturnType")
@Composable
fun MonthCalendarView(
    modifier: Modifier = Modifier,
    memos: List<MemoUI> = emptyList(),
    year: Int = LocalDate.now().year,
    month: Int = LocalDate.now().monthValue,
    date: LocalDate = LocalDate.now(),
    onSelectedDayChange: (LocalDate) -> Unit = { },
) {
    val context = LocalContext.current
    val yearMonth = YearMonth.of(year, month)
    val dayOfWeek = WeekFields.of(Locale.KOREAN).firstDayOfWeek

    val calendarView = remember {
        CalendarView(context).apply {
            orientation = LinearLayout.VERTICAL
            outDateStyle = OutDateStyle.EndOfGrid
            dayViewResource = R.layout.calendar_day
        }
    }

    LaunchedEffect(yearMonth) {
        calendarView.setup(yearMonth, yearMonth, dayOfWeek)
    }

    LaunchedEffect(memos, calendarView) {
        val dayBind = DayBind(calendarView, memos).apply {
            updateCalendar(LocalDate.of(date.year, month, date.dayOfMonth))
        }
        calendarView.dayBinder = dayBind.apply {
            input = object : DayBind.Input() {
                override fun onDayClick(date: LocalDate) {
                    onSelectedDayChange(date)
                    updateCalendar(date)
                }
            }
        }
    }

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp),
        factory = { calendarView },
    )
}