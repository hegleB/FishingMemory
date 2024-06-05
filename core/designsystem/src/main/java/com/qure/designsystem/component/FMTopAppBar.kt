package com.qure.designsystem.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.qure.designsystem.utils.FMPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FMTopAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    titleFontSize: TextUnit = 22.sp,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    navigationIconColor: Color = MaterialTheme.colorScheme.onBackground,
    containerColor: Color = MaterialTheme.colorScheme.background,
    onBack: () -> Unit = { },
    style: TextStyle = MaterialTheme.typography.displayLarge,
    actions: @Composable (RowScope.() -> Unit) = { },
) {
    CenterAlignedTopAppBar(
        modifier = modifier
            .fillMaxWidth(),
        title = {
            Text(
                text = title,
                style = style,
                fontSize = titleFontSize,
                color = titleColor,
            )
        },
        navigationIcon = {
            FMBackButton(
                onClickBack = { onBack() },
                iconColor = MaterialTheme.colorScheme.onBackground,
            )
        },
        actions = actions,
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = containerColor,
            navigationIconContentColor = navigationIconColor,
        ),
    )
}

@Preview
@Composable
private fun FMTopAppBarPreview() = FMPreview {
    FMTopAppBar()
}
