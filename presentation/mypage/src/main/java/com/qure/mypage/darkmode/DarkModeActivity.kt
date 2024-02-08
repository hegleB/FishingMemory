package com.qure.mypage.darkmode

import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qure.core.BaseComposeActivity
import com.qure.core_design.compose.theme.FishingMemoryTheme
import com.qure.domain.THEME_DARK
import com.qure.domain.THEME_SYSTEM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DarkModeActivity : BaseComposeActivity() {
    private val viewModel by viewModels<DarkModeViewModel>()

    @Composable
    override fun Screen() {
        val themeMode by viewModel.currentThemeMode.collectAsStateWithLifecycle()
        FishingMemoryTheme(
            darkTheme = if (themeMode == THEME_SYSTEM) isSystemInDarkTheme() else THEME_DARK == themeMode,
            dynamicColor = THEME_SYSTEM == themeMode,
        ) {
            Surface(color = MaterialTheme.colorScheme.background) {
                DarkModeScreen(
                    viewModel = viewModel,
                    onBack = { finish() },
                    currentThemeMode = themeMode,
                )
            }
        }
    }
}
