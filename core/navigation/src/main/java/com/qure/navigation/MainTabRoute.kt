package com.qure.navigation

import com.qure.model.FishingSpotUI
import com.qure.ui.model.MemoUI
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

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
        val memo: MemoUI = MemoUI(),
        val isEdit: Boolean = false,
    ) : Route {
        val route: String = "${this::class.qualifiedName}?memo={memo}"

        companion object {
            val typeMap = mapOf(typeOf<MemoUI>() to serializableType<MemoUI>())
        }
    }

    @Serializable
    data class FishingSpot(
        val fishingSpot: FishingSpotUI,
    ) : Route {

        val route: String = "${this::class.qualifiedName}?fishingSpot={fishingSpot}"

        companion object {
            val typeMap = mapOf(typeOf<FishingSpotUI>() to serializableType<FishingSpotUI>())
        }
    }

    @Serializable
    data object MemoList : Route

    @Serializable
    data class ProgramInformation(
        val url: String,
    ) : Route {
        val route: String = "${this::class.qualifiedName}?url={url}"
    }

    @Serializable
    data class MemoDetail(
        val memo: MemoUI = MemoUI(),
        val isEdit: Boolean = false,
        val isOpenDeepLink: Boolean = false,
    ) : Route {

        val route: String = "${this::class.qualifiedName}?memo={memo}&isEdit={isEdit}"

        companion object {
            val typeMap = mapOf(
                typeOf<MemoUI>() to serializableType<MemoUI>()
            )
        }
    }

    @Serializable
    data class MemoCreate(
        val memo: MemoUI = MemoUI(),
        val isEdit: Boolean = false,
    ) : Route {
        val route: String = "${this::class.qualifiedName}?memo={memo}&isEdit={isEdit}"

        companion object {
            val typeMap = mapOf(typeOf<MemoUI>() to serializableType<MemoUI>())
        }
    }

    @Serializable
    data class LocationSetting(
        val memo: MemoUI = MemoUI(),
        val isEdit: Boolean = false,
    ) : Route {
        val route: String = "${this::class.qualifiedName}?memo={memo}"

        companion object {
            val typeMap = mapOf(typeOf<MemoUI>() to serializableType<MemoUI>())
        }
    }

    @Serializable
    data object Bookmark : Route

    @Serializable
    data object DarkMode : Route

    @Serializable
    data class Camera(
        val memo: MemoUI = MemoUI(),
        val isEdit: Boolean = false,
    ) : Route {
        val route: String = "${this::class.qualifiedName}?memo={memo}&isEdit={isEdit}"

        companion object {
            val typeMap = mapOf(typeOf<MemoUI>() to serializableType<MemoUI>())
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