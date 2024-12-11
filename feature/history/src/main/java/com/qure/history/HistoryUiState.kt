package com.qure.history

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.qure.ui.model.MemoUI
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
sealed interface HistoryUiState {
    @Immutable
    data object Loading : HistoryUiState

    @Immutable
    data class Success(
        val memos: ImmutableList<MemoUI> = persistentListOf(),
    ) : HistoryUiState
}