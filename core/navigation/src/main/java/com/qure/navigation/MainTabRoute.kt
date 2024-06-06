package com.qure.navigation

import com.qure.ui.model.MemoUI
import com.qure.ui.model.toMemoString
import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Splash : Route

    @Serializable
    data object Onboarding : Route

    @Serializable
    data object Permission : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object Map : Route

    @Serializable
    data class Gallery(
        val memo: String = MemoUI().toMemoString()
    ) : Route

    @Serializable
    data class FishingSpot(val fishingSpot: String) : Route

    @Serializable
    data object MemoList : Route

    @Serializable
    data class ProgramInformation(val url: String) : Route

    @Serializable
    data class MemoDetail(
        val memo: String = MemoUI().toMemoString(),
        val isEdit: Boolean = false
    ) : Route

    @Serializable
    data class MemoCreate(
        val memo: String = MemoUI().toMemoString(),
        val isEdit: Boolean = false,
    ) : Route

    @Serializable
    data class LocationSetting(
        val memo: String = MemoUI().toMemoString(),
    ) : Route

    @Serializable
    data object Bookmark : Route

    @Serializable
    data object DarkMode : Route
}

sealed interface MainTabRoute : Route {
    @Serializable
    data object Home : MainTabRoute

    @Serializable
    data object History : MainTabRoute

    @Serializable
    data object MyPage : MainTabRoute
}