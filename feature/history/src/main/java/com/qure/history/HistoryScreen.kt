package com.qure.history

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qure.designsystem.component.FMMapButton
import com.qure.designsystem.component.FMMemoItem
import com.qure.designsystem.component.FMProgressBar
import com.qure.designsystem.component.FMRefreshLayout
import com.qure.designsystem.component.FMYearPickerDialog
import com.qure.designsystem.theme.Blue400
import com.qure.designsystem.theme.Blue600
import com.qure.designsystem.theme.Gray300
import com.qure.designsystem.utils.FMPreview
import com.qure.feature.history.R
import com.qure.ui.component.MonthCalendarView
import com.qure.ui.model.MemoUI
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun HistoryRoute(
    padding: PaddingValues,
    navigateToMap: () -> Unit,
    navigateToMemoDetail: (MemoUI) -> Unit,
    navigateToMemoCreate: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    viewModel: HistoryViewModel = hiltViewModel(),
) {

    LaunchedEffect(viewModel.error) {
        viewModel.error.collectLatest(onShowErrorSnackBar)
    }

    val filteredMemosUiState by viewModel.filteredMemosUiState.collectAsStateWithLifecycle()
    val dateUiState by viewModel.dateUiState.collectAsStateWithLifecycle()
    var isRefresh by remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.fetchFilteredMemos()
    }

    HistoryScreen(
        modifier = Modifier
            .padding(padding),
        uiState = filteredMemosUiState,
        year = dateUiState.year,
        month = dateUiState.month,
        date = dateUiState.date,
        showYearDialog = dateUiState.shouldShowYear,
        navigateToMap = navigateToMap,
        navigateToMemoDetail = navigateToMemoDetail,
        navigateToMemoCreate = navigateToMemoCreate,
        onSelectedDayChange = viewModel::selectDate,
        onSelectedMonthChange = viewModel::selectMonth,
        onSelectedYearChange = viewModel::selectYear,
        onRefresh = {
            viewModel.fetchFilteredMemos()
            coroutineScope.launch {
                isRefresh = true
                delay(1_000L)
                isRefresh = filteredMemosUiState is HistoryUiState.Loading
            }
        },
        shouldShowYearDialog = viewModel::shouldShowYear,
        isRefresh = isRefresh,
    )
}

@SuppressLint("DefaultLocale")
@Composable
private fun HistoryScreen(
    uiState: HistoryUiState = HistoryUiState.Success(),
    modifier: Modifier = Modifier,
    year: Int = LocalDate.now().year,
    month: Int = LocalDate.now().month.value.minus(1),
    date: LocalDate = LocalDate.now(),
    showYearDialog: Boolean = false,
    navigateToMap: () -> Unit = { },
    navigateToMemoDetail: (MemoUI) -> Unit = { },
    navigateToMemoCreate: () -> Unit = { },
    onSelectedDayChange: (LocalDate) -> Unit = { },
    onSelectedMonthChange: (Int) -> Unit = { },
    onSelectedYearChange: (Int) -> Unit = { },
    onRefresh: () -> Unit = { },
    shouldShowYearDialog: (Boolean) -> Unit = { },
    isRefresh: Boolean = false,
) {
    val state = rememberLazyListState()

    val isLoading = uiState is HistoryUiState.Loading
    if (showYearDialog) {
        FMYearPickerDialog(
            title = stringResource(id = R.string.numberpicker_title),
            year = year,
            selection = stringResource(id = R.string.numberpicker_selection),
            cancel = stringResource(id = R.string.numberpicker_cancel),
            onSelectedYearChanged = {
                shouldShowYearDialog(false)
                onSelectedYearChange(it)
            },
            onDismissRequest = { shouldShowYearDialog(false) }
        )
    }
    Column(
        modifier = modifier
            .padding(horizontal = 25.dp)
            .background(color = MaterialTheme.colorScheme.background),
    ) {

        FMRefreshLayout(
            onRefresh = { onRefresh() },
            isRefresh = isRefresh,
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(3.5f),
                state = state,
            ) {
                item {
                    HistoryTopAppBar(
                        year = year,
                        onClickMap = navigateToMap,
                        onClickYear = {
                            onSelectedYearChange(it)
                            shouldShowYearDialog(true)
                        },
                    )
                    HistoryMonthTabRow(
                        onSelectedMonthChange = { onSelectedMonthChange(it) },
                        monthIndex = month,
                    )
                    HistoryCalendar(
                        onSelectedDayChange = onSelectedDayChange,
                        year = year,
                        month = YearMonth.of(year, month.plus(1)),
                        date = date,
                        memos = if (uiState is HistoryUiState.Success) uiState.memos.filter {
                            it.date.startsWith(
                                "$year/${String.format("%02d", month.plus(1))}"
                            )
                        }.toPersistentList() else persistentListOf(),
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2f),
            ) {
                if (isLoading && isRefresh.not()) {
                    FMProgressBar(
                        modifier = Modifier
                            .align(Alignment.Center),
                    )
                }
                val selectedDate =
                    "$year/${String.format("%02d", month.plus(1))}/${
                        String.format("%02d", date.dayOfMonth)
                    }"
                MemoList(
                    modifier = Modifier
                        .align(Alignment.TopStart),
                    navigateToMemoDetail = navigateToMemoDetail,
                    selectedDayMemos = if (uiState is HistoryUiState.Success) {
                        uiState.memos.filter { it.date.startsWith(selectedDate) }
                    } else {
                        emptyList()
                    },
                )

                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(24.dp),
                    onClick = { navigateToMemoCreate() },
                    shape = CircleShape.copy(
                        bottomStart = CornerSize(0.dp),
                    ),
                    containerColor = Blue400,
                ) {
                    Icon(
                        painter = painterResource(id = com.qure.core.designsystem.R.drawable.ic_add),
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
            TabRowDefaults.PrimaryIndicator(
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
    date: LocalDate = LocalDate.now(),
    memos: ImmutableList<MemoUI> = persistentListOf(),
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
        date = date,
        onSelectedDayChange = onSelectedDayChange,
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
    modifier: Modifier = Modifier,
    selectedDayMemos: List<MemoUI> = emptyList(),
    navigateToMemoDetail: (MemoUI) -> Unit = { },
) {
    val scrollState = rememberLazyListState()
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
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

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 10.dp),
            )
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
        HistoryScreen()
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
//        selectedDayMemos = listOf(
//            MemoUI(
//                title = "물고기1"
//            ),
//            MemoUI(
//                title = "물고기2"
//            )
//        )
    )
}