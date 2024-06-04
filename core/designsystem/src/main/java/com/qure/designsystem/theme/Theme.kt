package com.qure.designsystem.theme

import android.app.Activity
import android.content.ContextWrapper
import android.os.Build
import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Gray700,
    onPrimary = White,
    secondary = Gray600,
    onSecondary = White,
    tertiary = Gray500,
    onTertiary = White,
    background = Black,
    onBackground = White,
    surface = Gray300,
    onSurface = Black,
    error = Color(0xFFB00020),
    onError = White,
    outline = White,
    surfaceTint = Black,
)

private val LightColorScheme = lightColorScheme(
    primary = Blue600,
    onPrimary = Black,
    secondary = Blue500,
    onSecondary = Black,
    tertiary = Blue400,
    onTertiary = Black,
    background = White,
    onBackground = Black,
    surface = Gray200,
    onSurface = Black,
    error = Color(0xFFB00020),
    onError = Black,
    outline = Gray700,
    surfaceTint = GrayBackground,
)

@Composable
fun FishingMemoryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )

    val view = LocalView.current
    SideEffect {
        val window = view.findActivity()?.window ?: return@SideEffect
        window.statusBarColor = Color.White.toArgb()
        WindowCompat.getInsetsController(window, view)?.isAppearanceLightStatusBars = true
    }
}

private fun View.findActivity(): Activity? {
    var context = this.context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}
