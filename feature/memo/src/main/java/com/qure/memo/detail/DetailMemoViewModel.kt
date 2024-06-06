package com.qure.memo.detail

import androidx.lifecycle.viewModelScope
import com.qure.domain.usecase.memo.DeleteMemoUseCase
import com.qure.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMemoViewModel
@Inject
constructor(
    private val deleteMemoUseCase: DeleteMemoUseCase,
) : BaseViewModel() {
    private val _detailMemoUiState = MutableStateFlow<DetailMemoUiState>(DetailMemoUiState.Initial)
    val detailMemoUiState = _detailMemoUiState.asStateFlow()

    fun deleteMemo(uuid: String) {
        viewModelScope.launch {
            deleteMemoUseCase(uuid)
                .onStart { DetailMemoUiState.Loading }
                .catch { throwable -> sendErrorMessage(throwable) }
                .collect {
                    _detailMemoUiState.value = DetailMemoUiState.Success
                }
        }
    }
}