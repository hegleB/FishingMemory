package com.qure.ui.component

import android.view.ContextThemeWrapper
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.DatePicker
import android.widget.LinearLayout
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.qure.core.ui.R
import com.qure.designsystem.component.FMButton
import com.qure.designsystem.theme.Blue500
import com.qure.designsystem.theme.Gray200
import com.qure.designsystem.theme.White
import com.qure.designsystem.utils.FMPreview
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FMCalendarDialog(
    modifier: Modifier = Modifier,
    cancel: String = "",
    selection: String = "",
    onDismissRequest: () -> Unit = { },
    onDateSelected: (String) -> Unit = { },
    date: String = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA).format(Date()),
) {
    var (year, month, day) = date.split("/").map { it.toInt() }

    Dialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(10.dp))
                .background(
                    color = MaterialTheme.colorScheme.background,
                ),
        ) {
            AndroidView(
                factory = { context ->
                    DatePicker(ContextThemeWrapper(context, R.style.CustomDatePicker))
                }
            ) { datePicker ->
                with(datePicker) {
                    init(year, month.minus(1), day) { _, selectedYear, selectedMonth, selectedDay ->
                        year = selectedYear
                        month = selectedMonth.plus(1)
                        day = selectedDay
                    }
                    layoutParams =
                        LinearLayout.LayoutParams(
                            (context.resources.displayMetrics.widthPixels * 0.8).toInt(),
                            WRAP_CONTENT
                        )
                }
            }

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp, end = 10.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                FMButton(
                    modifier = modifier
                        .border(
                            border = BorderStroke(width = 1.dp, color = Gray200),
                            shape = CircleShape,
                        )
                        .height(30.dp),
                    text = cancel,
                    onClick = { onDismissRequest() },
                    textStyle = MaterialTheme.typography.displaySmall,
                )

                Spacer(
                    modifier = modifier
                        .width(10.dp)
                )

                FMButton(
                    modifier = modifier
                        .height(30.dp),
                    text = selection,
                    onClick = {
                        val monthString = String.format("%02d", month)
                        val dayString = String.format("%02d", day)
                        onDateSelected("$year/$monthString/$dayString")
                        onDismissRequest()
                    },
                    fontColor = White,
                    buttonColor = Blue500,
                    textStyle = MaterialTheme.typography.displaySmall,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun FMCalendarDialogPreview() = FMPreview {
    FMCalendarDialog()
}
