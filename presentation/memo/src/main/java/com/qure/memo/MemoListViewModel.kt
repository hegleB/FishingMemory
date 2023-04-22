package com.qure.memo

import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.domain.entity.memo.*
import com.qure.domain.repository.AuthRepository
import com.qure.domain.usecase.memo.GetFilteredMemoUseCase
import com.qure.memo.model.MemoUI
import com.qure.memo.model.toMemoUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoListViewModel @Inject constructor(
    private val getFilteredMemoUseCase: GetFilteredMemoUseCase,
    private val authRepository: AuthRepository,
) : BaseViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState>
        get() = _uiState

    fun getFilteredMemo() {
        viewModelScope.launch {
            getFilteredMemoUseCase(
                getStructuredQuery()
            ).collect { response ->
                response.onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            isFilterInitialized = true,
                            filteredMemo = result.map { it.toMemoUI() }
                        )
                    }
                }.onFailure { throwable ->
                    sendErrorMessage(throwable)
                }
            }
        }
    }

    private fun getStructuredQuery(): MemoQuery {
        val emailFilter = FieldFilter(
            field = FieldPath(EMAIL),
            op = EQUAL,
            value = Value(authRepository.getEmailFromLocal())
        )

        val compositeFilter = CompositeFilter(
            op = AND,
            filters = listOf(Filter(emailFilter))
        )

        return MemoQuery(
            StructuredQuery(
                from = listOf(CollectionId(COLLECTION_ID)),
                where = Where(compositeFilter),
                orderBy = listOf(OrderBy(FieldPath(DATE), DESCENDING))
            )
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