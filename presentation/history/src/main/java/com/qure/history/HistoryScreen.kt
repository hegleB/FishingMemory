package com.qure.history

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.widget.LinearLayout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.view.CalendarView
import com.qure.core_design.compose.components.FMMapButton
import com.qure.core_design.compose.components.FMMemoItem
import com.qure.core_design.compose.components.FMProgressBar
import com.qure.core_design.compose.components.FMRefreshLayout
import com.qure.core_design.compose.components.FMYearPickerDialog
import com.qure.core_design.compose.theme.Blue400
import com.qure.core_design.compose.theme.Blue600
import com.qure.core_design.compose.theme.Gray300
import com.qure.core_design.compose.utils.FMPreview
import com.qure.history.view.DayBind
import com.qure.memo.model.MemoUI
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale


@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    navigateToMap: () -> Unit,
    navigateToMemoDetail: (MemoUI) -> Unit,
    navigateToMemoCreate: () -> Unit,
    onSelectedDayChange: (LocalDate) -> Unit,
    onSelectedMonthChange: (Int) -> Unit,
    onSelectedYearChange: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedDayMemos by viewModel.selectedDayMemos.collectAsStateWithLifecycle()
    val selectedYear by viewModel.selectedYear.collectAsStateWithLifecycle()
    val selectedMonth by viewModel.selectedMonth.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getFilteredMemo()
    }

    LaunchedEffect(selectedDate) {
        viewModel.getFilteredDayMemo(selectedDate)
    }

    HistoryContent(
        memos = uiState.filteredMemos,
        selectedDayMemos = selectedDayMemos,
        isFiltered = uiState.isFiltered,
        year = selectedYear ?: LocalDate.now().year,
        month = selectedMonth ?: LocalDate.now().month.value,
        navigateToMap = navigateToMap,
        navigateToMemoDetail = navigateToMemoDetail,
        navigateToMemoCreate = navigateToMemoCreate,
        onSelectedDayChange = onSelectedDayChange,
        onSelectedMonthChange = onSelectedMonthChange,
        onSelectedYearChange = onSelectedYearChange,
        onRefresh = { viewModel.getFilteredMemo() }
    )
}

@Composable
private fun HistoryContent(
    modifier: Modifier = Modifier,
    memos: List<MemoUI> = emptyList(),
    isFiltered: Boolean = false,
    selectedDayMemos: List<MemoUI> = emptyList(),
    year: Int = LocalDate.now().year,
    month: Int = LocalDate.now().month.value,
    navigateToMap: () -> Unit = { },
    navigateToMemoDetail: (MemoUI) -> Unit = { },
    navigateToMemoCreate: () -> Unit = { },
    onSelectedDayChange: (LocalDate) -> Unit = { },
    onSelectedMonthChange: (Int) -> Unit = { },
    onSelectedYearChange: (Int) -> Unit = { },
    onRefresh: () -> Unit = { },
) {
    val state = rememberLazyListState()

    var showYearDialog by remember {
        mutableStateOf(false)
    }

    if (showYearDialog) {
        FMYearPickerDialog(
            title = stringResource(id = R.string.numberpicker_title),
            year = year,
            selection = stringResource(id = R.string.numberpicker_selection),
            cancel = stringResource(id = R.string.numberpicker_cancel),
            onSelectedYearChanged = {
                showYearDialog = false
                onSelectedYearChange(it)
            },
            onDismissRequest = { showYearDialog = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 25.dp),
    ) {
        FMRefreshLayout(
            onRefresh = { onRefresh() },
        ) {
            LazyColumn(
                modifier = modifier,
                state = state,
            ) {
                item {
                    HistoryTopAppBar(
                        year = year,
                        onClickMap = navigateToMap,
                        onClickYear = {
                            onSelectedYearChange(it)
                            showYearDialog = true
                        },
                    )
                    HistoryMonthTabRow(
                        onSelectedMonthChange = { onSelectedMonthChange(it) },
                        monthIndex = month.minus(1),
                    )
                    HistoryCalendar(
                        onSelectedDayChange = onSelectedDayChange,
                        year = year,
                        month = YearMonth.of(year, month.plus(1)),
                        memos = memos,
                    )
                }
            }

            Box(
                modifier = modifier
                    .fillMaxSize(),
            ) {
                if (isFiltered) {
                    MemoList(
                        modifier = modifier,
                        isFiltered = isFiltered,
                        navigateToMemoDetail = navigateToMemoDetail,
                        selectedDayMemos = selectedDayMemos,
                    )

                } else {
                    FMProgressBar(
                        modifier = modifier
                            .align(Alignment.Center),
                    )
                }

                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 24.dp),
                    onClick = { navigateToMemoCreate() },
                    shape = CircleShape.copy(
                        bottomStart = CornerSize(0.dp),
                    ),
                    containerColor = Blue400,
                ) {
                    Icon(
                        painter = painterResource(id = com.qure.core_design.R.drawable.ic_add),
                        contentDescription = null,
                        tint = Color.White,
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryTopAppBar(
    modifier: Modifier = Modifier,
    year: Int,
    onClickYear: (Int) -> Unit = { },
    onClickMap: () -> Unit = { },
) {
    Box(
        modifier = modifier
            .padding(top = 20.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = year.toString(),
            modifier = modifier
                .align(Alignment.CenterStart)
                .clickable {
                    onClickYear(year)
                },
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 25.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )

        FMMapButton(
            modifier = modifier
                .size(25.dp)
                .align(Alignment.CenterEnd),
            onClickMap = onClickMap,
            iconColor = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun HistoryMonthTabRow(
    modifier: Modifier = Modifier,
    onSelectedMonthChange: (Int) -> Unit = { },
    monthIndex: Int = 0,
) {
    var state by remember { mutableIntStateOf(monthIndex) }

    LaunchedEffect(state) {
        onSelectedMonthChange(state)
    }

    val month = listOf(
        R.string.january_month,
        R.string.february_month,
        R.string.march_month,
        R.string.april_month,
        R.string.may_month,
        R.string.june_month,
        R.string.july_month,
        R.string.august_month,
        R.string.september_month,
        R.string.october_month,
        R.string.november_month,
        R.string.december_month,
    )

    ScrollableTabRow(
        modifier = modifier
            .fillMaxWidth(),
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        selectedTabIndex = state,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[state])
                    .padding(horizontal = 40.dp),
                height = 3.dp,
                color = Blue600,
            )
        },
        divider = { },
        tabs = {
            month.forEachIndexed { index, month ->
                Tab(
                    modifier = Modifier
                        .widthIn(min = 100.dp),
                    selected = state == index,
                    onClick = {
                        state = index
                    },
                    text = {
                        Text(
                            text = stringResource(id = month),
                            style = MaterialTheme.typography.displayLarge,
                            color = if (state == index) MaterialTheme.colorScheme.onBackground else Gray300
                        )
                    }
                )
            }
        }
    )
}

@Composable
private fun HistoryCalendar(
    modifier: Modifier = Modifier,
    onSelectedDayChange: (LocalDate) -> Unit = { },
    year: Int = LocalDate.now().year,
    month: YearMonth = YearMonth.of(year, LocalDate.now().month),
    memos: List<MemoUI> = emptyList(),
) {
    WeekCalendar(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
    )
    MonthCalendarView(
        modifier = modifier,
        memos = memos,
        year = year,
        month = month.monthValue,
        onSelectedDayChange = onSelectedDayChange,
    )
}

@SuppressLint("RememberReturnType")
@Composable
private fun MonthCalendarView(
    modifier: Modifier = Modifier,
    memos: List<MemoUI> = emptyList(),
    year: Int = LocalDate.now().year,
    month: Int = LocalDate.now().monthValue,
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

    LaunchedEffect(yearMonth, dayOfWeek) {
        calendarView.setup(yearMonth, yearMonth, dayOfWeek)
    }

    LaunchedEffect(memos, calendarView) {
        calendarView.dayBinder = DayBind(calendarView, memos).apply {
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

@Composable
private fun WeekCalendar(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Week.entries.forEach { week ->
            Text(
                modifier = modifier
                    .weight(1f),
                text = stringResource(id = week.dayId),
                style = MaterialTheme.typography.displayMedium,
                color = if (week == Week.SUN) Color.Red else MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun MemoList(
    isFiltered: Boolean = false,
    modifier: Modifier = Modifier,
    selectedDayMemos: List<MemoUI> = emptyList(),
    navigateToMemoDetail: (MemoUI) -> Unit = { },
) {
    val scrollState = rememberLazyListState()
    if (isFiltered) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth(),
            state = scrollState,
        ) {
            items(
                items = selectedDayMemos,
            ) { memo ->
                FMMemoItem(
                    title = memo.title,
                    content = memo.content,
                    location = memo.location,
                    fishType = memo.fishType,
                    date = memo.date,
                    imageUrl = memo.image,
                    onMemoClicked = { navigateToMemoDetail(memo) }
                )
            }
        }
    }
}

enum class Week(val dayId: Int) {
    SUN(R.string.sunday),
    MON(R.string.monday),
    TUE(R.string.tuesday),
    WED(R.string.wednesday),
    THU(R.string.thursday),
    FRI(R.string.friday),
    SAT(R.string.saturday),
}

@Preview
@Composable
private fun HistoryScreenPreview() = FMPreview {
    Surface {
        HistoryContent()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun HistoryMonthTabRowPreView() = FMPreview {
    HistoryMonthTabRow()
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun HistoryMemoListPreView() = FMPreview {
    MemoList(
        selectedDayMemos = listOf(
            MemoUI(
                title = "물고기1"
            ),
            MemoUI(
                title = "물고기2"
            )
        )
    )
}