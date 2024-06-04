package com.qure.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.qure.designsystem.utils.FMPreview
import com.qure.designsystem.theme.Gray100
import com.qure.designsystem.theme.Gray700
import com.qure.designsystem.theme.White

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FMMemoItem(
    modifier: Modifier = Modifier,
    imageUrl: String = "",
    title: String = "",
    location: String = "",
    fishType: String = "",
    content: String = "",
    date: String = "",
    onMemoClicked: () -> Unit = { },
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onMemoClicked() },
    ) {
        GlideImage(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(
                    width = 1.dp,
                    color = Gray100,
                    shape = RoundedCornerShape(10.dp),
                ),
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier
                .padding(start = 20.dp, end = 10.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterStart),
                    text = title,
                    style = MaterialTheme.typography.displayMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .wrapContentHeight()
                        .align(Alignment.CenterEnd)
                        .background(
                            color = Gray700,
                            shape = RoundedCornerShape(15.dp),
                        ),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(vertical = 5.dp, horizontal = 8.dp)
                            .align(Alignment.Center),
                        text = fishType,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        color = White,
                        fontSize = 13.sp,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            Text(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .width(130.dp)
                    .wrapContentHeight(),
                text = location,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground,
            )


            Text(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .width(130.dp)
                    .wrapContentHeight(),
                text = content,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Text(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .wrapContentHeight()
                    .align(Alignment.End),
                text = date,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true)
@Composable
private fun MemoItemPreview() = FMPreview {
    FMMemoItem(
        title = "물고기를 잡다",
        location = "서울특별시 강남구",
        content = "내용내용",
        fishType = "미꾸라지",
        date = "2024/05/01",
    )
}