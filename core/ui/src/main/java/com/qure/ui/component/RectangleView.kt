package com.qure.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import com.qure.model.camera.ObjectRect
import com.qure.model.camera.toFloatRect

@Composable
fun RectangleView(
    modifier: Modifier = Modifier,
    borderColor: Color,
    size: ObjectRect<Int>,
) {
    val (top, bottom, left, right) = size.toFloatRect()
    Canvas(
        modifier = modifier,
    ) {
        drawRect(
            color = borderColor,
            topLeft = Offset(x = left, y = top),
            size = Size(
                width = right - left, height = bottom - top
            ),
            style = Stroke(width = 15f),
        )
    }
}