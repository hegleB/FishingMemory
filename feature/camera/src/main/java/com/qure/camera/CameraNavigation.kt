package com.qure.camera

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.qure.navigation.Route
import com.qure.ui.model.MemoUI
import com.qure.ui.model.toMemoString
import com.qure.ui.model.toMemoUI

fun NavController.navigateCamera(memoUI: MemoUI, isEdit: Boolean) {
    navigate(Route.Camera(memoUI.toMemoString(), isEdit))
}

fun NavGraphBuilder.cameraNavHost(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    navigateToMemoCreate: (MemoUI) -> Unit,
) {
    composable<Route.Camera> { navBackStackEntry ->
        val memo = navBackStackEntry.toRoute<Route.MemoCreate>().memo.toMemoUI()
        CameraRoute(
            onShowErrorSnackBar = onShowErrorSnackBar,
            memoUI = memo,
            navigateToMemoCreate = navigateToMemoCreate,
        )
    }
}