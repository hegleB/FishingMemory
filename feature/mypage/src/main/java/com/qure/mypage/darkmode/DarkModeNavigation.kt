package com.qure.mypage.darkmode

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.qure.navigation.Route.DarkMode as DarkModeRoute

fun NavController.navigateDarkMode() {
    navigate(DarkModeRoute)
}

fun NavGraphBuilder.darkModeNavGraph(
    onBack: () -> Unit,
) {
    composable<DarkModeRoute> {
        DarkModeRoute(
            onBack = onBack,
        )
    }
}