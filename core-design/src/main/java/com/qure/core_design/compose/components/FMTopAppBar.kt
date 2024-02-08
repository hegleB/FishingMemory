package com.qure.core_design.compose.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.qure.core_design.R
import com.qure.core_design.compose.utils.FMPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FMTopAppBar(
    title: String,
    titleFontSize: TextUnit = TextUnit.Unspecified,
    titleColor: Color = Color.Unspecified,
    navigationIconColor: Color = Color.Unspecified,
    containerColor: Color = Color.Unspecified,
    onBack: () -> Unit,
    onClick: () -> Unit = { },
    hasActionIcon: Boolean = false,
    @DrawableRes actionIconRes: Int? = null,
    actions: @Composable (RowScope.() -> Unit) = { },
) {
    CenterAlignedTopAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.displayLarge,
                fontSize = titleFontSize,
                color = titleColor,
            )
        },
        navigationIcon = {
            FMBackButton(
                onClickBack = { onBack() },
                backIcon = painterResource(id = R.drawable.ic_arrow_back),
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
    FMTopAppBar(
        title = "TopAppBar",
        onBack = { },
        onClick = { },
    )
}
