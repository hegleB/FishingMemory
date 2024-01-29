package com.qure.history

import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.core.extensions.Dash
import com.qure.core.extensions.Slash
import com.qure.domain.entity.memo.*
import com.qure.domain.repository.AuthRepository
import com.qure.domain.usecase.memo.GetFilteredMemoUseCase
import com.qure.memo.MemoListViewModel.Companion.AND
import com.qure.memo.MemoListViewModel.Companion.COLLECTION_ID
import com.qure.memo.MemoListViewModel.Companion.DATE
import com.qure.memo.MemoListViewModel.Companion.DESCENDING
import com.qure.memo.MemoListViewModel.Companion.EMAIL
import com.qure.memo.MemoListViewModel.Companion.EQUAL
import com.qure.memo.model.MemoUI
import com.qure.memo.model.toMemoUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel
    @Inject
    constructor(
        private val getFilteredMemoUseCase: GetFilteredMemoUseCase,
        private val authRepository: AuthRepository,
    ) : BaseViewModel() {
        private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
        val uiState: StateFlow<UiState>
            get() = _uiState

        private val _selectedDayMemos: MutableStateFlow<List<MemoUI>> = MutableStateFlow(emptyList())
        val selectedDayMemos: StateFlow<List<MemoUI>>
            get() = _selectedDayMemos

        private val _selectedDate: MutableStateFlow<LocalDate> = MutableStateFlow(LocalDate.now())
        val selectedDate: StateFlow<LocalDate>
            get() = _selectedDate

        private val _selectedYearEvent: MutableSharedFlow<Int> = MutableSharedFlow()
        val selectedYearEvent: SharedFlow<Int>
            get() = _selectedYearEvent

        private val _pickedYear: MutableStateFlow<Int> = MutableStateFlow(LocalDate.now().year)
        val pickedYear: StateFlow<Int>
            get() = _pickedYear

        private val _selectedYear: MutableStateFlow<Int?> = MutableStateFlow(null)
        val selectedYear: StateFlow<Int?>
            get() = _selectedYear

        private val _selectedMonth: MutableStateFlow<Int?> = MutableStateFlow(null)
        val selectedMonth: StateFlow<Int?>
            get() = _selectedMonth

        fun getFilteredDayMemo(date: LocalDate) {
            viewModelScope.launch {
                getFilteredMemoUseCase(
                    getDayStructuredQuery(date),
                ).collect { response ->
                    response.onSuccess { result ->
                        _selectedDayMemos.value = result.map { it.toMemoUI() }
                    }.onFailure { throwable ->
                        sendErrorMessage(throwable)
                    }
                }
            }
        }

        fun getFilteredMemo() {
            viewModelScope.launch {
                getFilteredMemoUseCase(
                    getMonthStructuredQuery(),
                ).collect { response ->
                    response.onSuccess { result ->
                        _uiState.update {
                            it.copy(
                                isFiltered = true,
                                filteredMemos = result.map { it.toMemoUI() },
                            )
                        }
                    }.onFailure { throwable ->
                        sendErrorMessage(throwable)
                    }
                }
            }
        }

        private fun getMonthStructuredQuery(): MemoQuery {
            val emailFilter =
                FieldFilter(
                    field = FieldPath(EMAIL),
                    op = EQUAL,
                    value = Value(authRepository.getEmailFromLocal()),
                )

            val compositeFilter =
                CompositeFilter(
                    op = AND,
                    filters =
                        listOf(
                            Filter(emailFilter),
                        ),
                )

            return MemoQuery(
                StructuredQuery(
                    from = listOf(CollectionId(COLLECTION_ID)),
                    where = Where(compositeFilter),
                    orderBy = listOf(OrderBy(FieldPath(DATE), DESCENDING)),
                ),
            )
        }

        private fun getDayStructuredQuery(date: LocalDate): MemoQuery {
            val emailFilter =
                FieldFilter(
                    field = FieldPath(EMAIL),
                    op = EQUAL,
                    value = Value(authRepository.getEmailFromLocal()),
                )

            val dateFilter =
                FieldFilter(
                    field = FieldPath(DATE),
                    op = EQUAL,
                    value = Value(date.toString().split(String.Dash).joinToString(String.Slash)),
                )

            val compositeFilter =
                CompositeFilter(
                    op = AND,
                    filters =
                        listOf(
                            Filter(emailFilter),
                            Filter(dateFilter),
                        ),
                )

            return MemoQuery(
                StructuredQuery(
                    from = listOf(CollectionId(COLLECTION_ID)),
                    where = Where(compositeFilter),
                    orderBy = listOf(OrderBy(FieldPath(CREATE_TIME), DESCENDING)),
                ),
            )
        }

        fun selectDate(date: LocalDate) {
            _selectedDate.value = date
        }

        fun pickYear(year: Int) {
            _pickedYear.value = year
        }

        fun selectYear(year: Int) {
            _selectedYear.value = year
        }

        fun selectMonth(month: Int) {
            _selectedMonth.value = month
        }

        fun onYearSelectEvent(year: Int) {
            viewModelScope.launch {
                _selectedYearEvent.emit(year)
            }
        }

        companion object {
            const val CREATE_TIME = "createTime"
        }
    }

data class UiState(
    val isFiltered: Boolean = false,
    val filteredMemos: List<MemoUI> = emptyList(),
)
