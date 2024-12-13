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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MemoListViewModel
@Inject
constructor(
    private val getFilteredMemoUseCase: GetFilteredMemoUseCase,
) : BaseViewModel() {

    private val _memoListUiState = MutableStateFlow<MemoListUiState>(MemoListUiState.Loading)
    val memoListUiState = _memoListUiState.asStateFlow()

    fun fetchMemoList() {
        getFilteredMemoUseCase()
            .map { memos ->
                MemoListUiState.Success(memos.map { it.toMemoUI() }.toPersistentList())
            }
            .catch { throwable -> sendErrorMessage(throwable) }
            .onEach { newState ->
                _memoListUiState.value = MemoListUiState.Success(newState.memos.toPersistentList())
            }
            .launchIn(viewModelScope)
    }
}