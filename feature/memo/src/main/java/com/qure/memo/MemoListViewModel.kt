package com.qure.memo

import androidx.lifecycle.viewModelScope
import com.qure.domain.usecase.memo.GetFilteredMemoUseCase
import com.qure.ui.base.BaseViewModel
import com.qure.ui.model.toMemoUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoListViewModel
@Inject
constructor(
    private val getFilteredMemoUseCase: GetFilteredMemoUseCase,
) : BaseViewModel() {

    private val _memoListUiState = MutableStateFlow<MemoListUiState>(MemoListUiState.Loading)
    val memoListUiState = _memoListUiState.asStateFlow()

    init {
        fetchMemoList()
    }
    fun fetchMemoList() {
        viewModelScope.launch {
            getFilteredMemoUseCase()
                .map { memos -> MemoListUiState.Success(memos.map { it.toMemoUI() }.toPersistentList()) }
                .onStart { _memoListUiState.value = MemoListUiState.Loading }
                .catch { throwable -> sendErrorMessage(throwable) }
                .collectLatest { memoListUiState ->
                    _memoListUiState.value = memoListUiState
                }
        }
    }
}