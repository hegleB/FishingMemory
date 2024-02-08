package com.qure.core_design.compose.utils

import androidx.compose.runtime.Composable
import com.qure.core_design.compose.theme.FishingMemoryTheme

@Composable
fun FMPreview(isDarkTheme: Boolean = false, content: @Composable () -> Unit) {
    FishingMemoryTheme(darkTheme = isDarkTheme, content = content)
}
