package com.qure.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun CameraFrameCorners(
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
    ) {
        drawAllCorners(
            width = size.width,
            height = size.height,
            cornerRadius = 50f,
            lineStrokeWidth = 15f,
        )
    }
}
fun DrawScope.drawCorner(
    color: Color,
    centerX: Float,
    centerY: Float,
    startAngle: Float,
    sweepAngle: Float,
    cornerRadius: Float,
    lineStrokeWidth: Float
) {
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft = Offset(centerX, centerY),
        size = Size(cornerRadius * 2, cornerRadius * 2),
        style = Stroke(width = lineStrokeWidth)
    )

    drawLine(
        color = color,
        start = Offset(x = centerX + cornerRadius, y = centerY),
        end = Offset(x = centerX + cornerRadius * 2, y = centerY),
        strokeWidth = lineStrokeWidth
    )

    drawLine(
        color = color,
        start = Offset(x = centerX, y = centerY + cornerRadius),
        end = Offset(x = centerX, y = centerY + cornerRadius * 2),
        strokeWidth = lineStrokeWidth
    )
}

fun DrawScope.drawAllCorners(
    width: Float,
    height: Float,
    cornerRadius: Float,
    lineStrokeWidth: Float
) {
    val white = Color.White

    drawCorner(
        color = white,
        centerX = width * 0.25f,
        centerY = height * 0.25f,
        startAngle = 180f,
        sweepAngle = 90f,
        cornerRadius = cornerRadius,
        lineStrokeWidth = lineStrokeWidth
    )

    drawCorner(
        color = white,
        centerX = width * 1.5f,
        centerY = height * 0.25f,
        startAngle = 270f,
        sweepAngle = 90f,
        cornerRadius = cornerRadius,
        lineStrokeWidth = lineStrokeWidth
    )

    drawCorner(
        color = white,
        centerX = width * 0.25f,
        centerY = height * 1.5f,
        startAngle = 90f,
        sweepAngle = 90f,
        cornerRadius = cornerRadius,
        lineStrokeWidth = lineStrokeWidth
    )

    drawCorner(
        color = white,
        centerX = width * 1.5f,
        centerY = height * 1.5f,
        startAngle = 0f,
        sweepAngle = 90f,
        cornerRadius = cornerRadius,
        lineStrokeWidth = lineStrokeWidth
    )
}