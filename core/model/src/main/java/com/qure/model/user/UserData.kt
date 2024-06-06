package com.qure.model.user

import com.qure.model.darkmode.DarkModeConfig
import com.qure.model.fishingspot.FishingSpotBookmark

data class UserData(
    val email: String = "",
    val darkModeConfig: DarkModeConfig = DarkModeConfig.SYSTEM,
    val bookmarks: List<FishingSpotBookmark> = emptyList(),
    val splashState: SplashState = SplashState.OPEN,
    val isKakaoDeepLink: Boolean = false,
)

enum class SplashState {
    OPEN,
    SKIP,
    LOGGED_IN,
    NOT_LOGGED_IN,
    DEEPLINK,
}
