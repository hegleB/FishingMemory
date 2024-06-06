package com.qure.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.qure.navigation.MainTabRoute
import com.qure.ui.model.MemoUI

fun NavController.navigationHome(navOptions: NavOptions) {
    navigate(MainTabRoute.Home, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    padding: PaddingValues,
    navigateToMemoList: () -> Unit,
    navigateToDetailMemo: (MemoUI) -> Unit,
    navigateToMap: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable<MainTabRoute.Home> {
        HomeRoute(
            padding = padding,
            navigateToMemoList = navigateToMemoList,
            navigateToDetailMemo = navigateToDetailMemo,
            navigateToMap = navigateToMap,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}