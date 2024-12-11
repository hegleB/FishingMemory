package com.qure.ui.component

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qure.designsystem.component.FMGlideImage
import com.qure.designsystem.utils.clickableWithoutRipple

@Composable
fun PolaroidFrame(
    modifier: Modifier = Modifier,
    fishType: String = "",
    waterType: String = "",
    fishSize: String = "",
    title: String = "",
    location: String = "",
    content: String = "",
    date: String = "",
    imageUrl: String = "",
) {
    var rotationState by rememberSaveable { mutableFloatStateOf(0f) }
    val animatedRotation by animateFloatAsState(
        targetValue = rotationState,
        animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing),
        label = "PolaroidFrameRotationLabel",
    )

    val isFrontVisible = animatedRotation % 360 < 90 || animatedRotation % 360 > 270

    Box(
        modifier = modifier
            .graphicsLayer {
                rotationY = animatedRotation
                cameraDistance = 12f * density
            }
            .background(
                color = Color.White,
            )
            .clickableWithoutRipple {
                rotationState += 180f
            },
        contentAlignment = Alignment.Center
    ) {
        if (isFrontVisible) {
            CardFront(
                fishType = fishType,
                waterType = waterType,
                size = fishSize,
                imageUrl = imageUrl,
            )
        } else {
            CardBack(
                title = title,
                location = location,
                content = content,
                date = date,
            )
        }
    }
}

@Composable
fun CardFront(
    modifier: Modifier = Modifier,
    fishType: String = "",
    waterType: String = "",
    size: String = "",
    imageUrl: String = "",
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(
                        constraints.copy(maxHeight = constraints.maxHeight - 50.dp.roundToPx())
                    )
                    layout(placeable.width, placeable.height) {
                        placeable.placeRelative(0, 0)
                    }
                }
                .padding(top = 12.dp, start = 12.dp, end = 12.dp)
                .background(color = Color.Black),
        ) {
            FMGlideImage(
                model = imageUrl,
                contentDescription = "fish",
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                contentScale = ContentScale.FillWidth,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp),
        ) {
            if (fishType.isNotEmpty() && waterType.isNotEmpty()) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterStart),
                    text = "$fishType / $waterType",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            Spacer(modifier = Modifier.width(30.dp))
            if (size.isNotEmpty()) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterEnd),
                    text = "${size}CM",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}


@Composable
private fun CardBack(
    modifier: Modifier = Modifier,
    title: String = "",
    location: String = "",
    content: String = "",
    date: String = "",
) {
    Box(
        modifier = modifier
            .clipToBounds()
    ) {
        FishingMemoryBackSide(
            modifier = Modifier.fillMaxSize(),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationY = 180f
                }
                .padding(32.dp)
        ) {
            Text(
                text = "제목",
                style = MaterialTheme.typography.displayLarge,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(top = 10.dp, bottom = 10.dp),
                text = title,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = "위치",
                style = MaterialTheme.typography.displayLarge,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(top = 10.dp, bottom = 10.dp),
                text = location,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = "내용",
                style = MaterialTheme.typography.displayLarge,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 10.dp, bottom = 10.dp),
                text = content,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        Text(
            modifier = Modifier
                .width(100.dp)
                .height(50.dp)
                .padding(top = 10.dp, bottom = 10.dp)
                .align(Alignment.BottomStart)
                .graphicsLayer {
                    rotationY = 180f
                },
            text = date,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun FishingMemoryBackSide(
    modifier: Modifier = Modifier,
    fontSize: Float = 50f,
) {
    Canvas(
        modifier = modifier
            .graphicsLayer {
                rotationZ = 50f
                rotationY = 180f
            }
    ) {
        val textPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.argb(70, 160, 120, 80) // 텍스트 색상
            textSize = fontSize
            isAntiAlias = true
        }

        val text = "FishingMemory"
        val textWidth = textPaint.measureText(text)

        val step = 500f
        for (x in -size.width.toInt()..size.width.toInt() step step.toInt()) {
            for (y in -size.height.toInt()..size.height.toInt() step step.toInt()) {
                drawContext.canvas.nativeCanvas.drawText(
                    text,
                    x.toFloat() - textWidth / 2,
                    y.toFloat(),
                    textPaint
                )
            }
        }
    }
}

