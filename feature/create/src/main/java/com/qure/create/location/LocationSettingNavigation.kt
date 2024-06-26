package com.qure.create.location

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.qure.navigation.Route
import com.qure.ui.model.MemoUI
import com.qure.ui.model.toMemoString
import com.qure.ui.model.toMemoUI
import com.qure.navigation.Route.LocationSetting as LocationSettingRoute

fun NavController.navigateLocationSetting(memoUI: MemoUI, navOptions: NavOptions) {
    navigate(Route.LocationSetting(memoUI.toMemoString()), navOptions)
}

fun NavGraphBuilder.locationSettingNavGraph(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    navigateToMemoCreate: (MemoUI) -> Unit,
    onBack: () -> Unit,
) {
    composable<LocationSettingRoute> { navBackStackEntry ->
        val memo = navBackStackEntry.toRoute<Route.MemoCreate>().memo.toMemoUI()
        LocationSettingRoute(
            memo = memo,
            onBack = onBack,
            navigateToMemoCreate = navigateToMemoCreate,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}