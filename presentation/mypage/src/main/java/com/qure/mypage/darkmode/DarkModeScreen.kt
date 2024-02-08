package com.qure.mypage.darkmode

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qure.core_design.compose.components.FMTopAppBar
import com.qure.core_design.compose.theme.Blue600
import com.qure.core_design.compose.theme.Gray500
import com.qure.core_design.compose.utils.FMPreview
import com.qure.core_design.compose.utils.clickableWithoutRipple
import com.qure.domain.THEME_DARK
import com.qure.domain.THEME_LIGHT
import com.qure.domain.THEME_SYSTEM
import com.qure.mypage.R
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DarkModeScreen(
    viewModel: DarkModeViewModel,
    onBack: () -> Unit,
    currentThemeMode: String,
) {
    var currentThemeMode by remember { mutableStateOf(currentThemeMode) }
    LaunchedEffect(viewModel.currentThemeMode) {
        viewModel.currentThemeMode.collectLatest {
            currentThemeMode = it
        }
    }
    DarkModeContent(
        modifier = Modifier.fillMaxSize(),
        onBack = onBack,
        currentThemeMode = currentThemeMode,
        onClick = { mode ->
            viewModel.setDarkMode(mode)
            currentThemeMode = mode
        },
    )
}

@Composable
private fun DarkModeContent(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    currentThemeMode: String,
    onClick: (String) -> Unit,
) {
    val modeTypes = mapOf(
        THEME_SYSTEM to stringResource(id = R.string.theme_system_mode),
        THEME_DARK to stringResource(id = R.string.theme_dark_mode),
        THEME_LIGHT to stringResource(id = R.string.theme_light_mode),
    )

    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        FMTopAppBar(
            title = stringResource(id = R.string.dark_mode_setting),
            onBack = { onBack() },
            titleFontSize = 22.sp,
            titleColor = MaterialTheme.colorScheme.onBackground,
            navigationIconColor = MaterialTheme.colorScheme.onBackground,
        )
        modeTypes.forEach { (modeType, mode) ->
            DarkModeItem(
                mode = mode,
                modeType = modeType,
                state = modeType == currentThemeMode,
                onClick = onClick,
            )
        }
    }
}

@Composable
private fun DarkModeItem(
    mode: String,
    modeType: String,
    state: Boolean,
    onClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 20.dp)
            .clickableWithoutRipple { onClick(modeType) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = mode,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.weight(1f))
        if (state) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        color = Blue600,
                        shape = CircleShape,
                    ),
            ) {
                Icon(
                    painter = painterResource(id = com.qure.core_design.R.drawable.ic_check),
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .border(
                        width = 2.dp,
                        color = Gray500,
                        shape = CircleShape,
                    ),
            )
        }
    }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DarkModeScreenDarkModePreview() = FMPreview(isDarkTheme = true) {
    DarkModeContent(
        onBack = { },
        currentThemeMode = stringResource(id = R.string.theme_dark_mode),
        onClick = { },
    )
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
private fun DarkModeScreenLightModePreview() = FMPreview {
    DarkModeContent(
        onBack = { },
        currentThemeMode = stringResource(id = R.string.theme_dark_mode),
        onClick = { },
    )
}
