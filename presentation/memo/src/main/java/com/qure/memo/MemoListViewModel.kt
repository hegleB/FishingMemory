package com.qure.memo

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
import com.qure.memo.model.MemoUI
import com.qure.memo.model.toMemoUI
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val authRepository: AuthRepository,
) : BaseViewModel() {

    private val _memoListUiState = MutableStateFlow<MemoListUiState>(MemoListUiState.Loading)
    val memoListUiState = _memoListUiState.asStateFlow()

    init {
        fetchMemoList()
    }
    fun fetchMemoList() {
        viewModelScope.launch {
            getFilteredMemoUseCase(getStructuredQuery())
                .map { memos -> MemoListUiState.Success(memos.map { it.toMemoUI() }) }
                .onStart { _memoListUiState.value = MemoListUiState.Loading }
                .catch { throwable -> sendErrorMessage(throwable) }
                .collectLatest { memoListUiState ->
                    _memoListUiState.value = memoListUiState
                }
        }
    }
    private fun getStructuredQuery(): MemoQuery {
        val emailFilter =
            FieldFilter(
                field = FieldPath(EMAIL),
                op = EQUAL,
                value = Value(authRepository.getEmailFromLocal()),
            )

        val compositeFilter =
            CompositeFilter(
                op = AND,
                filters = listOf(Filter(emailFilter)),
            )

        return MemoQuery(
            StructuredQuery(
                from = listOf(CollectionId(COLLECTION_ID)),
                where = Where(compositeFilter),
                orderBy = listOf(OrderBy(FieldPath(DATE), DESCENDING)),
            ),
        )
    }

    companion object {
        const val EMAIL = "email"
        const val DATE = "date"
        const val DESCENDING = "DESCENDING"
        const val EQUAL = "EQUAL"
        const val AND = "AND"
        const val COLLECTION_ID = "memo"
    }
}

data class UiState(
    val isFilterInitialized: Boolean = false,
    val filteredMemo: List<MemoUI> = emptyList(),
)
