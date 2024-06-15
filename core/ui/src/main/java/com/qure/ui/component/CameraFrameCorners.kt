package com.qure.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.qure.designsystem.theme.White

@Composable
fun CameraFrameCorners(
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
    ) {
        val width = size.width * 0.55f
        val height = size.height * 0.4f
        val cornerRadius = 30.dp.toPx()

        val topLeftCenterX = width * 0.25f
        val topLeftCenterY = height * 0.25f

        drawArc(
            color = White,
            startAngle = 180f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(topLeftCenterX, topLeftCenterY),
            size = Size(cornerRadius * 2, cornerRadius * 2),
            style = Stroke(width = 15f)
        )

        drawLine(
            color = White,
            start = Offset(x = topLeftCenterX + cornerRadius, y = topLeftCenterY),
            end = Offset(x = topLeftCenterX + cornerRadius * 2, y = topLeftCenterY),
            strokeWidth = 15f
        )

        drawLine(
            color = White,
            start = Offset(x = topLeftCenterX, y = topLeftCenterY + cornerRadius),
            end = Offset(x = topLeftCenterX, y = topLeftCenterY + cornerRadius * 2),
            strokeWidth = 15f
        )

        val topRightCenterX = width * 1.5f
        val topRightCenterY = height * 0.25f

        drawArc(
            color = White,
            startAngle = 270f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(topRightCenterX, topRightCenterY),
            size = Size(cornerRadius * 2, cornerRadius * 2),
            style = Stroke(width = 15f)
        )

        drawLine(
            color = White,
            start = Offset(x = topRightCenterX, y = topRightCenterY),
            end = Offset(x = topRightCenterX + cornerRadius, y = topRightCenterY),
            strokeWidth = 15f
        )

        drawLine(
            color = White,
            start = Offset(
                x = topRightCenterX + cornerRadius * 2,
                y = topRightCenterY + cornerRadius
            ),
            end = Offset(
                x = topRightCenterX + cornerRadius * 2,
                y = topRightCenterY + cornerRadius * 2
            ),
            strokeWidth = 15f
        )

        val bottomLeftCenterX = width * 0.25f
        val bottomLeftCenterY = height * 1.5f

        drawArc(
            color = White,
            startAngle = 90f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(bottomLeftCenterX, bottomLeftCenterY),
            size = Size(cornerRadius * 2, cornerRadius * 2),
            style = Stroke(width = 15f)
        )

        drawLine(
            color = White,
            start = Offset(
                x = bottomLeftCenterX + cornerRadius * 2,
                y = bottomLeftCenterY + cornerRadius * 2
            ),
            end = Offset(
                x = bottomLeftCenterX + cornerRadius,
                y = bottomLeftCenterY + cornerRadius * 2
            ),
            strokeWidth = 15f
        )

        drawLine(
            color = White,
            start = Offset(x = bottomLeftCenterX, y = bottomLeftCenterY + cornerRadius),
            end = Offset(x = bottomLeftCenterX, y = bottomLeftCenterY),
            strokeWidth = 15f
        )

        val bottomRightCenterX = width * 1.5f
        val bottomRightCenterY = height * 1.5f

        drawArc(
            color = White,
            startAngle = 0f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(bottomRightCenterX, bottomRightCenterY),
            size = Size(cornerRadius * 2, cornerRadius * 2),
            style = Stroke(width = 15f)
        )

        drawLine(
            color = White,
            start = Offset(
                x = bottomRightCenterX + cornerRadius * 2,
                y = bottomRightCenterY + cornerRadius
            ),
            end = Offset(x = bottomRightCenterX + cornerRadius * 2, y = bottomRightCenterY),
            strokeWidth = 15f
        )

        drawLine(
            color = White,
            start = Offset(
                x = bottomRightCenterX + cornerRadius,
                y = bottomRightCenterY + cornerRadius * 2
            ),
            end = Offset(x = bottomRightCenterX, y = bottomRightCenterY + cornerRadius * 2),
            strokeWidth = 15f
        )
    }
}