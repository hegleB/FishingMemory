package com.qure.camera

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.qure.navigation.Route
import com.qure.ui.model.MemoUI
import com.qure.ui.model.SnackBarMessageType

fun NavController.navigateCamera(memoUI: MemoUI, isEdit: Boolean) {
    navigate(Route.Camera(memoUI, isEdit))
}

fun NavGraphBuilder.cameraNavHost(
    navigateToMemoCreate: (MemoUI, Boolean) -> Unit,
    onShowMessageSnackBar: (messageType: SnackBarMessageType) -> Unit,
) {
    composable<Route.Camera>(
        typeMap = Route.Camera.typeMap,
    ) { navBackStackEntry ->
        val memo = navBackStackEntry.toRoute<Route.MemoCreate>().memo
        val isEdit = navBackStackEntry.toRoute<Route.MemoCreate>().isEdit
        CameraRoute(
            memoUI = memo,
            navigateToMemoCreate = { memoUI ->navigateToMemoCreate(memoUI, isEdit) },
            onShowMessageSnackBar = onShowMessageSnackBar,
        )
    }
}