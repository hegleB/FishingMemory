package com.qure.core_design.compose.utils

import androidx.compose.runtime.Composable
import com.qure.core_design.compose.theme.FishingMemoryTheme

@Composable
fun FMPreview(content: @Composable () -> Unit) {
    FishingMemoryTheme(content = content)
}
