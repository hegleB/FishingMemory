package com.qure.core_design.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.qure.core_design.compose.theme.Blue600
import com.qure.core_design.compose.theme.Gray200
import com.qure.core_design.compose.theme.White

@Composable
fun FMChipGroup(
    modifier: Modifier = Modifier,
    elements: List<String> = emptyList(),
    onClickChip: (String) -> Unit = { },
    selectedChip: String = "",
) {
    LazyRow(
        modifier = modifier,
    ) {
        items(elements) { text ->
            FMChip(
                text = text,
                onClick = { onClickChip(text) },
                isSelected = selectedChip == text,
            )
            Spacer(modifier = Modifier.padding(5.dp))
        }
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun FMChip(
    text: String = "",
    onClick: (String) -> Unit = { },
    isSelected: Boolean = false,
) {
    Chip(
        modifier = Modifier
            .width(80.dp),
        onClick = { onClick(text) },
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = 2.dp,
            color = if (isSystemInDarkTheme()) White else Gray200,
        ),
        colors = ChipDefaults.chipColors(
            backgroundColor = if (isSelected) Blue600 else MaterialTheme.colorScheme.background,
        ),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = text,
            textAlign = TextAlign.Center,
            color = if (isSelected) White else MaterialTheme.colorScheme.onBackground,
        )
    }
}