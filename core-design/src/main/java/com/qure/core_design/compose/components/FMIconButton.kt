package com.qure.core_design.compose.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun FMBackButton(
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit,
    backIcon: Painter,
) {
    IconButton(
        modifier = modifier,
        onClick = { onClickBack() },
    ) {
        Icon(
            painter = backIcon,
            contentDescription = null,
        )
    }
}

@Composable
fun FMCircleAddButton(
    modifier: Modifier = Modifier,
    onClickAdd: () -> Unit,
    circleAddIcon: Painter,
) {
    IconButton(
        modifier = modifier,
        onClick = { onClickAdd() },
    ) {
        Icon(
            painter = circleAddIcon,
            contentDescription = null,
        )
    }
}
