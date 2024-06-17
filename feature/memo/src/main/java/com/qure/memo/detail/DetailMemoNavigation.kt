package com.qure.memo.detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.qure.navigation.Route
import com.qure.ui.model.MemoUI
import com.qure.ui.model.SnackBarMessageType
import com.qure.ui.model.toMemoUI
import com.qure.navigation.Route.MemoDetail as MemoDetailRoute

fun NavController.navigateMemoDetail(memo: String, navOptions: NavOptions?) {
    navigate(Route.MemoDetail(memo), navOptions)
}

fun NavGraphBuilder.memoDetailNavGraph(
    onBack: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onClickEdit: (MemoUI) -> Unit,
    onShowMessageSnackBar: (messageType: SnackBarMessageType) -> Unit,
) {
    composable<MemoDetailRoute> { navBackStackEntry ->
        val memo = navBackStackEntry.toRoute<Route.MemoDetail>().memo.toMemoUI()
        DetailMemoRoute(
            memo = memo,
            onBack = onBack,
            onClickEdit = onClickEdit,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onShowMessageSnackBar = onShowMessageSnackBar,
        )
    }
}