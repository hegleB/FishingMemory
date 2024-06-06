package com.qure.onboarding

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.qure.navigation.Route
import com.qure.navigation.Route.Onboarding as OnboardingRoute

fun NavController.navigateOnboarding() {
    navigate(OnboardingRoute) {
        popUpTo(Route.Splash) {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.onboardingNavGraph(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    navigateToPermission: () -> Unit,
) {
    composable<OnboardingRoute> {
        OnboardingRoute(
            navigateToPermission = navigateToPermission,
            onShowErrorSnackBar = onShowErrorSnackBar
        )
    }
}