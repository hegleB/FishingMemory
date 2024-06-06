package com.qure.main

import androidx.compose.runtime.Composable
import com.qure.core.designsystem.R
import com.qure.navigation.MainTabRoute

enum class MainTab(
    val iconRes: Int,
    val contentDescription: String,
    val route: MainTabRoute,
) {
    HOME(
        iconRes = R.drawable.ic_tab_home_unselected,
        contentDescription = "홈",
        MainTabRoute.Home,
    ),
    HISTORY(
        iconRes = R.drawable.ic_tab_history_selected,
        contentDescription = "나의 기록",
        MainTabRoute.History,
    ),
    MYPAGE(
        iconRes = R.drawable.ic_tab_mypage_unselected,
        contentDescription = "마이페이지",
        MainTabRoute.MyPage,
    );

    companion object {
        @Composable
        fun find(predicate: @Composable (MainTabRoute) -> Boolean): MainTab? {
            return entries.find { tab -> predicate(tab.route) }
        }

        @Composable
        fun contains(predicate: @Composable (MainTabRoute) -> Boolean): Boolean {
            return entries.map { tab -> tab.route }.any { predicate(it) }
        }
    }
}