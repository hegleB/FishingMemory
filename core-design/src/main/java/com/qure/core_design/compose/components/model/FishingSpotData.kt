package com.qure.core_design.compose.components.model

import androidx.annotation.DrawableRes

data class FishingSpotData(
    @DrawableRes val iconRes: Int?,
    val type: String,
    val description: String,
    val onClickPhoneNumber: () -> Unit = { },
)