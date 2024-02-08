package com.qure.core_design.compose.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun FMBackButton(
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit,
    backIcon: Painter,
    iconColor: Color = Color.Unspecified,
) {
    IconButton(
        modifier = modifier,
        onClick = { onClickBack() },
    ) {
        Icon(
            painter = backIcon,
            contentDescription = null,
            tint = iconColor,
        )
    }
}

@Composable
fun FMCircleAddButton(
    modifier: Modifier = Modifier,
    onClickAdd: () -> Unit,
    circleAddIcon: Painter,
    iconColor: Color = Color.Unspecified,
) {
    IconButton(
        modifier = modifier,
        onClick = { onClickAdd() },
    ) {
        Icon(
            painter = circleAddIcon,
            contentDescription = null,
            tint = iconColor,
        )
    }
}
