package com.qure.memo.detail

import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.core.extensions.Empty
import com.qure.domain.usecase.memo.DeleteMemoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMemoViewModel @Inject constructor(
    private val deleteMemoUseCase: DeleteMemoUseCase,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState>
        get() = _uiState

    fun deleteMemo(uuid: String) {
        viewModelScope.launch {
            deleteMemoUseCase(uuid).collect { response ->
                response.onSuccess {
                    _uiState.update {
                        it.copy(
                            isDeleteInitialized = true,
                            deleteSuccessMessage = DELETE_SUCCESS_MESSAGE
                        )
                    }

                }.onFailure { throwable ->
                    sendErrorMessage(throwable)
                }
            }
        }
    }

    companion object {
        const val DELETE_SUCCESS_MESSAGE = "삭제가 완료되었습니다"
    }
}

data class UiState(
    val isDeleteInitialized: Boolean = false,
    val deleteSuccessMessage: String = String.Empty,
)