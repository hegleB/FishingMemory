package com.qure.history

import androidx.lifecycle.viewModelScope
import com.qure.domain.usecase.memo.GetFilteredMemoUseCase
import com.qure.ui.base.BaseViewModel
import com.qure.ui.model.toMemoUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel
@Inject
constructor(
    private val getFilteredMemoUseCase: GetFilteredMemoUseCase,
) : BaseViewModel() {
    private val _filteredMemosUiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val filteredMemosUiState = _filteredMemosUiState.asStateFlow()

    private val _dateUiState = MutableStateFlow(DateUiState())
    val dateUiState = _dateUiState.asStateFlow()

    init {
        fetchFilteredMemos()
    }

    fun fetchFilteredMemos() {
        getFilteredMemoUseCase()
            .map { memos -> HistoryUiState.Success(memos.map { it.toMemoUI() }.toPersistentList()) }
            .catch { throwable -> sendErrorMessage(throwable) }
            .onEach { _filteredMemosUiState.value = it }
            .launchIn(viewModelScope)
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