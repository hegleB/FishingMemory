package com.qure.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.qure.designsystem.theme.Blue600

@Composable
fun Tape(
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier,
    ) {
        val topPath = Path().apply {
            moveTo(0f, 0f)
            for (i in 0..10) {
                val x = (size.width / 10) * i
                val y = if (i % 2 == 0) 5.dp.toPx() else 0f
                lineTo(x, y)
            }
            lineTo(0f, size.height)
            close()
        }

        val bottomPath = Path().apply {
            moveTo(0f, size.height)
            for (i in 0..10) {
                val x = (size.width / 10) * i
                val y = if (i % 2 == 0) size.height else size.height - 5.dp.toPx()
                lineTo(x, y)
            }
            lineTo(size.width, 0f)
            close()
        }

        drawPath(
            path = topPath,
            color = Blue600,
        )

        drawPath(
            path = bottomPath,
            color = Blue600,
        )
    }
}