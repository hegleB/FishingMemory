package com.qure.splash

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.qure.navigation.Route.Splash as SplashRoute

fun NavController.navigateSplash() {
    navigate(SplashRoute) {
        this.launchSingleTop = true
    }
}

fun NavGraphBuilder.splashNavGraph(
    navigateToOnBoarding: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToMemoDetail: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable<SplashRoute> {
        SplashRoute(
            navigateToOnBoarding = navigateToOnBoarding,
            navigateToLogin = navigateToLogin,
            navigateToHome = navigateToHome,
            navigateToMemoDetail = navigateToMemoDetail,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}