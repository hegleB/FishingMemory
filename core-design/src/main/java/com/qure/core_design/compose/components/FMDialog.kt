package com.qure.core_design.compose.components

import android.content.res.Configuration
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.DatePicker
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.qure.core_design.R
import com.qure.core_design.compose.theme.Blue200
import com.qure.core_design.compose.theme.Blue500
import com.qure.core_design.compose.theme.Blue600
import com.qure.core_design.compose.theme.Gray200
import com.qure.core_design.compose.theme.Gray300
import com.qure.core_design.compose.theme.Gray400
import com.qure.core_design.compose.theme.White
import com.qure.core_design.compose.utils.FMPreview
import com.qure.core_design.compose.utils.clickableWithoutRipple
import java.util.Calendar

@Composable
fun FMDeleteDialog(
    title: String = "",
    description: String = "",
    cancel: String = "",
    delete: String = "",
    onClickDelete: () -> Unit = { },
    onDismiss: () -> Unit = { },
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .width(300.dp),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(80.dp)
                    .background(color = Blue200)
                    .align(Alignment.CenterHorizontally),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = null,
                )
            }
            Text(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .align(Alignment.CenterHorizontally),
                text = title,
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .align(Alignment.CenterHorizontally),
                text = description,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Row(modifier = Modifier.padding(20.dp)) {
                TextButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 5.dp),
                    onClick = { onDismiss() },
                ) {
                    Text(
                        text = cancel,
                        style = MaterialTheme.typography.displayMedium,
                    )
                }
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(35.dp),
                    onClick = { onClickDelete(); onDismiss() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue600,
                    ),
                    shape = RoundedCornerShape(5.dp),
                ) {
                    Text(
                        text = delete,
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.White,
                    )
                }
            }
        }
    }
}

@Composable
fun FMShareDialog(
    title: String = "",
    kakaoTalkShare: String = "",
    moreShare: String = "",
    onClickKakao: () -> Unit = { },
    onClickMore: () -> Unit = { },
    onDismiss: () -> Unit = { },
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .width(280.dp),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 15.dp, bottom = 30.dp, end = 15.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.TopCenter),
                    text = title,
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 15.sp,
                )
                FMCloseButton(
                    modifier = Modifier
                        .size(25.dp)
                        .align(Alignment.TopEnd),
                    onClickClose = { onDismiss() },
                    iconColor = Gray300,
                )

                Row(
                    modifier = Modifier
                        .padding(top = 40.dp, start = 20.dp, end = 5.dp),
                ) {
                    ShareButton(
                        modifier = Modifier
                            .clickableWithoutRipple { onClickKakao() },
                        text = kakaoTalkShare,
                        shareImage = R.drawable.ic_kakao_share,
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    ShareButton(
                        modifier = Modifier
                            .clickableWithoutRipple { onClickMore() },
                        text = moreShare,
                        shareImage = R.drawable.ic_more,
                    )
                }
            }
        }
    }
}

@Composable
private fun ShareButton(
    modifier: Modifier = Modifier,
    text: String = "",
    @DrawableRes shareImage: Int,

    ) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier = Modifier
                .size(50.dp)
                .border(
                    border = BorderStroke(width = 0.5.dp, color = Gray400),
                    shape = CircleShape,
                ),
            painter = painterResource(id = shareImage),
            contentDescription = null,
        )
        Text(
            modifier = Modifier
                .padding(top = 5.dp)
                .align(Alignment.CenterHorizontally),
            text = text,
            fontSize = 12.sp,
        )
    }
}

@Composable
fun FMYearPickerDialog(
    title: String,
    cancel: String,
    selection: String,
    year: Int,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = { },
    onSelectedYearChanged: (Int) -> Unit,
) {
    var yearState by remember { mutableIntStateOf(year) }

    Dialog({ onDismissRequest() }) {
        Card(
            modifier = modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
            border = BorderStroke(width = 1.dp, color = Color.White),
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    text = title,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp,
                )

                FMYearPicker(
                    modifier = modifier
                        .width(150.dp)
                        .align(Alignment.CenterHorizontally),
                    value = yearState,
                    onValueChange = {
                        yearState = it
                    },
                )

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                ) {
                    FMButton(
                        modifier = modifier
                            .padding(end = 10.dp)
                            .weight(1f)
                            .border(
                                border = BorderStroke(width = 1.dp, color = Gray200),
                                shape = CircleShape,
                            )
                            .height(40.dp),
                        text = cancel,
                        onClick = { onDismissRequest() },
                    )

                    FMButton(
                        modifier = modifier
                            .height(40.dp)
                            .padding(start = 10.dp)
                            .weight(1f),
                        text = selection,
                        onClick = { onSelectedYearChanged(yearState) },
                        fontColor = Color.White,
                        buttonColor = Blue600,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FMCalendarDialog(
    modifier: Modifier = Modifier,
    cancel: String = "",
    selection: String = "",
    onDismissRequest: () -> Unit = { },
    onDateSelected: (String) -> Unit = { },
) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    DatePickerDialog(
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background,
            todayContentColor = White,
            yearContentColor = MaterialTheme.colorScheme.onBackground,
            dayContentColor = MaterialTheme.colorScheme.onBackground,
            selectedDayContainerColor = Blue600,
            currentYearContentColor = MaterialTheme.colorScheme.onBackground,
            selectedDayContentColor = White,
        ),
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            FMButton(
                modifier = modifier
                    .height(30.dp),
                text = selection,
                onClick = { },
                fontColor = Color.White,
                buttonColor = Blue500,
            )
        },
        dismissButton = {
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
        },
        shape = RoundedCornerShape(10.dp),
    ) {
        AndroidView(
            factory = { context ->
                DatePicker(ContextThemeWrapper(context, R.style.CustomDatePicker))
            }
        ) { datePicker ->
            with(datePicker) {
                init(year, month, day) { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDateStr = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                    onDateSelected(selectedDateStr)
                }
                layoutParams =
                    LinearLayout.LayoutParams(
                        (context.resources.displayMetrics.widthPixels * 0.78).toInt(),
                        WRAP_CONTENT
                    )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun FMBookmarkDeleteDialogPreview() = FMPreview {
    FMDeleteDialog(
        title = "북마크를 전체 삭제하시겠습니까?",
        description = "이 작업은 모든 북마크가 \n삭제됩니다.",
        cancel = "취소",
        delete = "삭제",
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun FMShareDialogPreview() = FMPreview {
    FMShareDialog(
        title = "공유하기",
        kakaoTalkShare = "카카오톡",
        moreShare = "더보기",
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun FMYearDialogPreview() = FMPreview {
    FMYearPickerDialog(
        title = "년도 선택",
        year = 2024,
        cancel = "취소",
        selection = "선택",
        onSelectedYearChanged = {},
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun FMCalendarDialogPreview() = FMPreview {
    FMCalendarDialog()
}
