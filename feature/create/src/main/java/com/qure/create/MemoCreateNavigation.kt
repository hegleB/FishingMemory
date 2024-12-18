package com.qure.create

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.qure.navigation.Route
import com.qure.ui.model.MemoUI
import com.qure.ui.model.SnackBarMessageType

fun NavController.navigateMemoCreate(
    memo: MemoUI,
    isEdit: Boolean,
    navOptions: NavOptions
) {
    navigate(Route.MemoCreate(memo = memo, isEdit = isEdit), navOptions)
}

fun NavGraphBuilder.memoCreateNavGraph(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onBack: () -> Unit,
    navigateToLocationSetting: (MemoUI, Boolean) -> Unit,
    navigateToGallery: (MemoUI, Boolean) -> Unit,
    navigateToMemoDetail: (MemoUI) -> Unit,
    onShowMessageSnackBar: (messageType: SnackBarMessageType) -> Unit,
) {
    composable<Route.MemoCreate>(
        typeMap = Route.MemoCreate.typeMap
    ) { navBackStackEntry ->
        val memo = navBackStackEntry.toRoute<Route.MemoDetail>().memo
        val isEdit = navBackStackEntry.toRoute<Route.MemoDetail>().isEdit
        MemoCreateRoute(
            memoUI = memo,
            isEdit = isEdit,
            onBack = onBack,
            navigateToLocationSetting = { memoUI -> navigateToLocationSetting(memoUI,isEdit) },
            navigateToGallery = { memoUI ->  navigateToGallery(memoUI, isEdit) },
            navigateToMemoDetail = navigateToMemoDetail,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onShowMessageSnackBar = onShowMessageSnackBar,
        )
    }
}