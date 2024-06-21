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
    data class FishingSpot(val fishingSpot: String) : Route {
        fun toRouteString():String {
            return "${this::class.qualifiedName}?fishingSpot={fishingSpot}"
        }
    }

    @Serializable
    data object MemoList : Route

    @Serializable
    data class ProgramInformation(val url: String) : Route {
        fun toRouteString():String {
            return "${this::class.qualifiedName}?url={url}"
        }
    }

    @Serializable
    data class MemoDetail(
        val memo: String = MemoUI().toMemoString(),
        val isEdit: Boolean = false
    ) : Route {
        fun toRouteString():String {
            return "${this::class.qualifiedName}?memo={memo}&isEdit={isEdit}"
        }
    }

    @Serializable
    data class MemoCreate(
        val memo: String = MemoUI().toMemoString(),
        val isEdit: Boolean = false,
    ) : Route {
        fun toRouteString():String {
            return "${this::class.qualifiedName}?memo={memo}&isEdit={isEdit}"
        }
    }

    @Serializable
    data class LocationSetting(
        val memo: String = MemoUI().toMemoString(),
    ) : Route {
        fun toRouteString():String {
            return "${this::class.qualifiedName}?memo={memo}"
        }
    }

    @Serializable
    data object Bookmark : Route

    @Serializable
    data object DarkMode : Route

    @Serializable
    data class Camera(
        val memo: String = MemoUI().toMemoString(),
        val isEdit: Boolean = false,
    ) : Route {
        fun toRouteString():String {
            return "${this::class.qualifiedName}?memo={memo}&isEdit={isEdit}"
        }
    }
}

sealed interface MainTabRoute : Route {
    @Serializable
    data object Home : MainTabRoute

    @Serializable
    data object History : MainTabRoute

    @Serializable
    data object MyPage : MainTabRoute
}