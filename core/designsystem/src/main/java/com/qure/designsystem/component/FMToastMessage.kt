package com.qure.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qure.designsystem.utils.FMPreview
import com.qure.designsystem.theme.Blue400
import com.qure.designsystem.theme.Gray200
import com.qure.designsystem.theme.Gray700
import kotlinx.coroutines.delay

@Composable
fun FMToastMessage(
    title: String?,
    duration: Long = 2000,
    isError: Boolean = false,
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize(),
    ) {
        if (title != null) {
            ToastMessage(
                message = title,
                isError = isError,
                duration = duration,
            )
        }
    }
}

@Composable
fun ToastMessage(
    message: String,
    isError: Boolean = false,
    duration: Long = 2000,
) {
    var isVisible by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = message) {
        delay(duration)
        isVisible = false
    }

    if (isError) {
        ToastMessage(message)
    } else {
        ErrorToastMessage(message)
    }
}

@Composable
private fun ToastMessage(message: String) {
    Surface(
        color = Gray700,
        shape = RoundedCornerShape(15.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 17.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF0080FF),
            )
            Text(
                text = message,
                modifier = Modifier.padding(start = 9.dp, top = 12.dp, bottom = 12.dp),
                color = Gray200,
            )
        }
    }
}

@Composable
private fun ErrorToastMessage(message: String) {
    Surface(
        color = Gray700,
        modifier = Modifier.padding(end = 17.dp),
        shape = RoundedCornerShape(15.dp),
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(vertical = 11.dp, horizontal = 20.dp),
            color = Blue400,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun ToastMessagePreview() = FMPreview {
    ToastMessage(message = "메모가 저장되었습니다")
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun ErrorToastMessagePreview() = FMPreview {
    ErrorToastMessage(message = "메모가 저장이 안됨")
}
