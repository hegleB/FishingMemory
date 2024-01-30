package com.qure.core_design.compose.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.qure.core_design.R

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
    ),
    displayMedium = defaultTextStyle.copy(
        fontFamily = FontFamily(Font(R.font.sc_bold_dream)),
        fontSize = 15.sp,
    ),
    defaultTextStyle.copy(
        fontFamily = FontFamily(Font(R.font.sc_medium_dream)),
        fontSize = 12.sp,
    ),
    defaultTextStyle.copy(
        fontFamily = FontFamily(Font(R.font.sc_black_dream)),
        fontSize = 14.sp,
        lineHeight = 22.sp,
    ),
    defaultTextStyle.copy(
        fontFamily = FontFamily(Font(R.font.sc_medium_dream)),
        fontSize = 15.sp,
        lineHeight = 24.sp,
    ),
    defaultTextStyle.copy(
        fontFamily = FontFamily(Font(R.font.sc_medium_dream)),
        fontSize = 14.sp,
    ),
    defaultTextStyle.copy(
        fontFamily = FontFamily(Font(R.font.sc_medium_dream)),
        fontSize = 13.sp,
        lineHeight = 20.sp,
    ),
)
