package com.qure.designsystem.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Modifier.clickableWithoutRipple(onClick: () -> Unit): Modifier {
    return this.clickable(
        interactionSource = MutableInteractionSource(),
        indication = null
    ) {
        onClick()
    }
}