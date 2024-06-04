package com.qure.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.qure.core.designsystem.R


val defaultTextStyle = TextStyle(
    platformStyle = PlatformTextStyle(
        includeFontPadding = false,
    ),
    letterSpacing = TextUnit.Unspecified,
    color = Gray700,
)

val Typography = Typography(
    displayLarge = defaultTextStyle.copy(
        fontFamily = FontFamily(Font(R.font.sc_bold_dream)),
        fontSize = 17.sp,
    ), // Title1
    displayMedium = defaultTextStyle.copy(
        fontFamily = FontFamily(Font(R.font.sc_bold_dream)),
        fontSize = 15.sp,
    ), // Title2
    displaySmall = defaultTextStyle.copy(
        fontFamily = FontFamily(Font(R.font.sc_medium_dream)),
        fontSize = 12.sp,
    ), // SubTitle1
    headlineLarge = defaultTextStyle.copy(
        fontFamily = FontFamily(Font(R.font.sc_black_dream)),
        fontSize = 14.sp,
        lineHeight = 22.sp,
    ), // Heading
    bodyLarge = defaultTextStyle.copy(
        fontFamily = FontFamily(Font(R.font.sc_medium_dream)),
        fontSize = 15.sp,
        lineHeight = 24.sp,
    ), // Paragraph1
    bodyMedium = defaultTextStyle.copy(
        fontFamily = FontFamily(Font(R.font.sc_medium_dream)),
        fontSize = 14.sp,
    ), // Paragraph2
    bodySmall = defaultTextStyle.copy(
        fontFamily = FontFamily(Font(R.font.sc_medium_dream)),
        fontSize = 13.sp,
        lineHeight = 20.sp,
    ), // Paragraph3
)
