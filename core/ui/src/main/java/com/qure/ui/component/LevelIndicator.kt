package com.qure.ui.component

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import com.qure.designsystem.theme.Blue500
import com.qure.designsystem.theme.White

@Composable
fun LevelIndicatorView(
    modifier: Modifier = Modifier,
    changeLevel: (Float, Float) -> Unit = { _, _ -> },
) {
    val sensorManager =
        LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    var centerX by remember { mutableFloatStateOf(0f) }
    var centerY by remember { mutableFloatStateOf(0f) }
    var sensorX by remember { mutableFloatStateOf(0f) }
    var sensorY by remember { mutableFloatStateOf(0f) }

    val sensorListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let { sensorEvent ->
                sensorX = sensorEvent.values[0] * 10
                sensorY = sensorEvent.values[1] * 10
                changeLevel(sensorX, sensorY)
            }
        }
    }

    DisposableEffect(Unit) {
        sensorManager.registerListener(
            sensorListener,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL,
        )

        onDispose {
            sensorManager.unregisterListener(sensorListener)
        }
    }


    Canvas(
        modifier = modifier,
        onDraw = {
            centerX = size.width / 3f * 1
            centerY = size.height / 3f * 2

            drawCircle(
                color = Blue500,
                radius = centerX * 0.25f - 3f,
                center = Offset(sensorX + centerX, sensorY + centerY),
            )

            drawLine(
                color = White,
                start = Offset(centerX, centerY + (centerX * 0.5f)),
                end = Offset(centerX, centerY - (centerX * 0.5f)),
                strokeWidth = 4f,
            )

            drawLine(
                color = White,
                start = Offset(centerX - (centerX * 0.5f), centerY),
                end = Offset(centerX + (centerX * 0.5f), centerY),
                strokeWidth = 4f,
            )

            drawCircle(
                color = White,
                radius = centerX * 0.5f,
                center = Offset(centerX, centerY),
                style = Stroke(width = 4f)
            )


            drawCircle(
                color = White,
                radius = centerX * 0.25f,
                center = Offset(centerX, centerY),
                style = Stroke(width = 4f)
            )
        }
    )
}