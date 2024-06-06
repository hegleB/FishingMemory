package com.qure.memo

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.qure.ui.model.MemoUI
import com.qure.navigation.Route.MemoList as MemoListRoute

fun NavController.navigateMemoList(navOptions: NavOptions) {
    navigate(MemoListRoute, navOptions)
}

fun NavGraphBuilder.memoListNavGraph(
    navigateToMemoCreate: () -> Unit,
    navigateToMemoDetail: (MemoUI) -> Unit,
    onBack: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable<MemoListRoute> {
        MemoListRoute(
            onBack = onBack,
            navigateToMemoCreate = navigateToMemoCreate,
            navigateToMemoDetail = navigateToMemoDetail,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}