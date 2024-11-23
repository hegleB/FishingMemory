package com.qure.history

import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
data class DateUiState(
    val date: LocalDate = LocalDate.now(),
    val year: Int = LocalDate.now().year,
    val month: Int = LocalDate.now().month.value.minus(1),
    val shouldShowYear: Boolean = false,
)