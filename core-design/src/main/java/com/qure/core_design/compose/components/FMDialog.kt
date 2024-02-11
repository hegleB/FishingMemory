package com.qure.core_design.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.Dialog
import com.qure.core_design.R
import com.qure.core_design.compose.theme.Blue200
import com.qure.core_design.compose.theme.Blue600
import com.qure.core_design.compose.utils.FMPreview

@Composable
fun FMBookmarkDeleteDialog(
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

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun FMBookmarkDeleteDialogPreview() = FMPreview {
    FMBookmarkDeleteDialog(
        title = "북마크를 전체 삭제하시겠습니까?",
        description = "이 작업은 모든 북마크가 \n삭제됩니다.",
        cancel = "취소",
        delete = "삭제",
    )
}
