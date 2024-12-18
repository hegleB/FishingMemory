package com.qure.create.location

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.qure.navigation.Route
import com.qure.ui.model.MemoUI
import com.qure.navigation.Route.LocationSetting as LocationSettingRoute

fun NavController.navigateLocationSetting(memoUI: MemoUI, isEdit: Boolean, navOptions: NavOptions) {
    navigate(Route.LocationSetting(memoUI, isEdit), navOptions)
}

fun NavGraphBuilder.locationSettingNavGraph(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    navigateToMemoCreate: (MemoUI, Boolean) -> Unit,
    onBack: () -> Unit,
) {
    composable<Route.LocationSetting>(
        typeMap = LocationSettingRoute.typeMap,
    ) { navBackStackEntry ->
        val memo = navBackStackEntry.toRoute<Route.MemoCreate>().memo
        val isEdit = navBackStackEntry.toRoute<Route.MemoCreate>().isEdit
        LocationSettingRoute(
            memo = memo,
            onBack = onBack,
            navigateToMemoCreate = { memoUI -> navigateToMemoCreate(memoUI, isEdit) },
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}