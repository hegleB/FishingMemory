package com.qure.history

import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.domain.entity.memo.CollectionId
import com.qure.domain.entity.memo.CompositeFilter
import com.qure.domain.entity.memo.FieldFilter
import com.qure.domain.entity.memo.FieldPath
import com.qure.domain.entity.memo.Filter
import com.qure.domain.entity.memo.MemoQuery
import com.qure.domain.entity.memo.OrderBy
import com.qure.domain.entity.memo.StructuredQuery
import com.qure.domain.entity.memo.Value
import com.qure.domain.entity.memo.Where
import com.qure.domain.repository.AuthRepository
import com.qure.domain.usecase.memo.GetFilteredMemoUseCase
import com.qure.memo.MemoListViewModel.Companion.AND
import com.qure.memo.MemoListViewModel.Companion.COLLECTION_ID
import com.qure.memo.MemoListViewModel.Companion.DATE
import com.qure.memo.MemoListViewModel.Companion.DESCENDING
import com.qure.memo.MemoListViewModel.Companion.EMAIL
import com.qure.memo.MemoListViewModel.Companion.EQUAL
import com.qure.memo.model.toMemoUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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
    private val _filteredMemosUiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Empty)
    val filteredMemosUiState = _filteredMemosUiState.asStateFlow()

    private val _dateUiState = MutableStateFlow(DateUiState())
    val dateUiState = _dateUiState.asStateFlow()

    init {
        fetchFilteredMemos()
    }

    fun fetchFilteredMemos() {
        viewModelScope.launch {
            getFilteredMemoUseCase(getMonthStructuredQuery())
                .map { memos -> HistoryUiState.Success(memos.map { it.toMemoUI() }) }
                .catch { throwable -> sendErrorMessage(throwable) }
                .onStart { _filteredMemosUiState.value = HistoryUiState.Loading }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = HistoryUiState.Empty,
                )
                .collectLatest { uiState ->
                    _filteredMemosUiState.value = uiState
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

    fun selectDate(date: LocalDate) {
        _dateUiState.update {
            it.copy(date = date)
        }
    }

    fun selectYear(year: Int) {
        _dateUiState.update {
            it.copy(year = year)
        }
    }

    fun selectMonth(month: Int) {
        _dateUiState.update {
            it.copy(month = month)
        }
    }

    fun shouldShowYear(isShown: Boolean) {
        _dateUiState.update {
            it.copy(shouldShowYear = isShown)
        }
    }
}