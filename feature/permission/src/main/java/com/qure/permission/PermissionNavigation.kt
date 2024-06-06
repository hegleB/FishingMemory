package com.qure.permission

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.qure.navigation.Route
import com.qure.navigation.Route.Permission as PermissionRoute

fun NavController.navigatePermission() {
    navigate(PermissionRoute) {
        popUpTo(Route.Onboarding) {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.permissionNavGraph(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    navigateToLogin: () -> Unit,
) {
    composable<PermissionRoute> {
        PermissionRoute(
            navigateToLogin = navigateToLogin,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}