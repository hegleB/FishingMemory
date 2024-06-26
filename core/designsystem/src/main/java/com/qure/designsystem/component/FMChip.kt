package com.qure.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qure.designsystem.theme.Black
import com.qure.designsystem.theme.Blue600
import com.qure.designsystem.theme.Gray200
import com.qure.designsystem.theme.White

enum class Orientation {
    Horizontal, Vertical
}

@Composable
fun FMChipGroup(
    modifier: Modifier = Modifier,
    elements: List<String> = emptyList(),
    onClickChip: (String) -> Unit = { },
    selectedChip: String = "",
    chipModifier: Modifier = Modifier.width(80.dp),
    chipFontSize: TextUnit = 15.sp,
    unSelectedFontColor: Color = Black,
    selectedFontColor: Color = White,
    selectedChipColor: Color = Blue600,
    unSelectedChipColor: Color = MaterialTheme.colorScheme.background,
    interval: Dp = 5.dp,
    borderColor: Color = if (isSystemInDarkTheme()) White else Gray200,
    chipTextStyle: TextStyle = TextStyle.Default,
    orientation: Orientation = Orientation.Horizontal,
) {
    when (orientation) {
        Orientation.Horizontal -> {
            LazyRow(
                modifier = modifier,
            ) {
                items(elements) { text ->
                    FMChip(
                        modifier = chipModifier,
                        text = text,
                        onClick = { onClickChip(text) },
                        isSelected = selectedChip == text,
                        fontSize = chipFontSize,
                        unSelectedFontColor = unSelectedFontColor,
                        selectedFontColor = selectedFontColor,
                        selectedChipColor = selectedChipColor,
                        unSelectedChipColor = unSelectedChipColor,
                        borderColor = borderColor,
                        chipTextStyle = chipTextStyle,
                    )
                    Spacer(modifier = Modifier.width(interval))
                }
            }
        }
        Orientation.Vertical -> {
            LazyColumn(
                modifier = modifier,
            ) {
                items(elements) { text ->
                    FMChip(
                        modifier = chipModifier,
                        text = text,
                        onClick = { onClickChip(text) },
                        isSelected = selectedChip == text,
                        fontSize = chipFontSize,
                        unSelectedFontColor = unSelectedFontColor,
                        selectedFontColor = selectedFontColor,
                        selectedChipColor = selectedChipColor,
                        unSelectedChipColor = unSelectedChipColor,
                        borderColor = borderColor,
                        chipTextStyle = chipTextStyle,
                    )
                    Spacer(modifier = Modifier.height(interval))
                }
            }
        }
    }
}

@Composable
fun FMChip(
    text: String,
    onClick: (String) -> Unit,
    isSelected: Boolean,
    modifier: Modifier,
    fontSize: TextUnit,
    unSelectedFontColor: Color,
    selectedFontColor: Color,
    selectedChipColor: Color,
    unSelectedChipColor: Color,
    borderColor: Color,
    chipTextStyle: TextStyle
) {
    AssistChip(
        modifier = modifier,
        onClick = { onClick(text) },
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = 2.dp,
            color = borderColor,
        ),
        label = {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = text,
                textAlign = TextAlign.Center,
                color = if (isSelected) selectedFontColor else unSelectedFontColor,
                fontSize = fontSize,
                style = chipTextStyle,
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (isSelected) selectedChipColor else unSelectedChipColor,
        ),
    )
}