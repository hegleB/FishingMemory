package com.qure.designsystem.utils

import androidx.compose.runtime.Composable
import com.qure.designsystem.theme.FishingMemoryTheme

@Composable
fun FMPreview(isDarkTheme: Boolean = false, content: @Composable () -> Unit) {
    FishingMemoryTheme(darkTheme = isDarkTheme, content = content)
}
