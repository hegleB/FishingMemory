package com.qure.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.qure.navigation.Route.Login as LoginRoute

fun NavController.navigateLogin(navOptions: NavOptions) {
    navigate(LoginRoute, navOptions)
}

fun NavGraphBuilder.loginNavGraph(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    navigateToHome: () -> Unit,
) {
    composable<LoginRoute> {
        LoginRoute(
            navigateToHome = navigateToHome,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}