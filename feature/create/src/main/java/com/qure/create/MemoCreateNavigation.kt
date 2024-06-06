package com.qure.create

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.qure.navigation.Route
import com.qure.ui.model.MemoUI
import com.qure.ui.model.toMemoUI

fun NavController.navigateMemoCreate(
    memo: String,
    isEdit: Boolean,
    navOptions: NavOptions
) {
    navigate(Route.MemoCreate(memo = memo, isEdit = isEdit), navOptions)
}

fun NavGraphBuilder.memoCreateNavGraph(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onBack: () -> Unit,
    navigateToLocationSetting: (MemoUI) -> Unit,
    navigateToGallery: (MemoUI) -> Unit,
    navigateToMemoDetail: (MemoUI) -> Unit,
) {
    composable<Route.MemoCreate> { navBackStackEntry ->
        val memo = navBackStackEntry.toRoute<Route.MemoDetail>().memo.toMemoUI()
        val isEdit = navBackStackEntry.toRoute<Route.MemoDetail>().isEdit
        MemoCreateRoute(
            memo = memo,
            isEdit = isEdit,
            onBack = onBack,
            navigateToLocationSetting = navigateToLocationSetting,
            navigateToGallery = navigateToGallery,
            navigateToMemoDetail = navigateToMemoDetail,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}