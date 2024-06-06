package com.qure.history

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.qure.navigation.MainTabRoute
import com.qure.ui.model.MemoUI

fun NavController.navigationHistory(navOptions: NavOptions) {
    navigate(MainTabRoute.History, navOptions)
}

fun NavGraphBuilder.historyNavGraph(
    padding: PaddingValues,
    navigateToMap: () -> Unit,
    navigateToMemoDetail: (MemoUI) -> Unit,
    navigateToMemoCreate: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable<MainTabRoute.History> {
        HistoryRoute(
            padding = padding,
            navigateToMap = navigateToMap,
            navigateToMemoDetail = navigateToMemoDetail,
            navigateToMemoCreate = navigateToMemoCreate,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}