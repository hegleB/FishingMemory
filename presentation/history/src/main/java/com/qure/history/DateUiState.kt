package com.qure.history

import java.time.LocalDate

data class DateUiState(
    val date: LocalDate = LocalDate.now(),
    val year: Int = LocalDate.now().year,
    val month: Int = LocalDate.now().month.value.minus(1),
    val shouldShowYear: Boolean = false,
)