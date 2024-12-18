package com.qure.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.mindsync.program_information.navigateProgramInformation
import com.qure.camera.navigateCamera
import com.qure.create.location.navigateBookmark
import com.qure.create.location.navigateLocationSetting
import com.qure.create.navigateMemoCreate
import com.qure.fishingspot.navigateFishingSpot
import com.qure.gallery.navigateGallery
import com.qure.history.navigationHistory
import com.qure.home.navigationHome
import com.qure.login.navigateLogin
import com.qure.map.navigateMap
import com.qure.memo.detail.navigateMemoDetail
import com.qure.memo.navigateMemoList
import com.qure.model.FishingSpotUI
import com.qure.mypage.darkmode.navigateDarkMode
import com.qure.mypage.navigationMyPage
import com.qure.navigation.MainTabRoute
import com.qure.navigation.Route
import com.qure.onboarding.navigateOnboarding
import com.qure.permission.navigatePermission
import com.qure.ui.model.MemoUI


class MainNavigator(
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val startDestination = Route.Splash

    val startDeppLinkDestination = MainTabRoute.Home

    val currentTab: MainTab?
        @Composable get() = MainTab.find { tab ->
            currentDestination?.hasRoute(tab::class) == true
        }

    fun navigate(tab: MainTab) {
        val navOptions = navOptions {
            popUpTo(MainTab.HOME.route) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (tab) {
            MainTab.HOME -> navController.navigationHome(
                navOptions = navOptions
            )

            MainTab.HISTORY -> navController.navigationHistory(
                navOptions = navOptions
            )

            MainTab.MYPAGE -> navController.navigationMyPage(
                navOptions = navOptions
            )
        }
    }

    fun navigateToOnboarding() {
        navController.navigateOnboarding()
    }

    fun navigateToPermission() {
        navController.navigatePermission()
    }

    fun navigateToLogin(navOptions: NavOptions) {
        navController.navigateLogin(navOptions)
    }

    fun navigateToHome(navOptions: NavOptions) {
        navController.navigationHome(navOptions)
    }

    fun navigateToMap() {
        navController.navigateMap()
    }

    fun navigateToFishingSpot(fishingSpotUI: FishingSpotUI) {
        navController.navigateFishingSpot(fishingSpotUI)
    }

    fun navigateToGallery(
        memoUI: MemoUI = MemoUI(),
        isEdit: Boolean = false,
        navOptions: NavOptions = navOptions { }
    ) {
        navController.navigateGallery(memoUI, isEdit, navOptions)
    }

    fun navigateToMemoList(navOptions: NavOptions = navOptions { }) {
        navController.navigateMemoList(navOptions)
    }

    fun navigateToMemoCreate(
        memoUI: MemoUI = MemoUI(),
        isEdit: Boolean = false,
        navOptions: NavOptions = navOptions { }
    ) {
        navController.navigateMemoCreate(
            memo = memoUI,
            isEdit = isEdit,
            navOptions = navOptions
        )
    }

    fun navigateToProgramInformation(url: String) {
        navController.navigateProgramInformation(url)
    }

    fun navigateToLocationSetting(
        memoUI: MemoUI = MemoUI(),
        isEdit: Boolean = false,
        navOptions: NavOptions = navOptions { }
    ) {
        navController.navigateLocationSetting(memoUI, isEdit, navOptions)
    }

    fun navigateToMemoDetail(
        memoUI: MemoUI,
        isOpenDeepLink: Boolean = false,
        navOptions: NavOptions? = null
    ) {
        navController.navigateMemoDetail(memoUI, isOpenDeepLink, navOptions)
    }

    fun navigateToBookmark() {
        navController.navigateBookmark()
    }

    fun navigateToDarkMode() {
        navController.navigateDarkMode()
    }

    fun navigateToCamera(memo: MemoUI, isEdit: Boolean = false) {
        navController.navigateCamera(memo, isEdit)
    }


    fun popBackStack() {
        navController.popBackStack()
    }

    fun popBackStackIfNotHome() {
        if (!isSameCurrentDestination<MainTabRoute.Home>()) {
            popBackStack()
        }
    }

    private inline fun <reified T : Route> isSameCurrentDestination(): Boolean {
        return navController.currentDestination?.hasRoute<T>() == true
    }

    @Composable
    fun shouldShowBottomBar() = MainTab.contains {
        currentDestination?.hasRoute(it::class) == true
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}