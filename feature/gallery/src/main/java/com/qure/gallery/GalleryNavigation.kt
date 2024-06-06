package com.qure.gallery

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.qure.navigation.Route
import com.qure.ui.model.MemoUI
import com.qure.ui.model.toMemoString
import com.qure.ui.model.toMemoUI
import com.qure.navigation.Route.Gallery as GalleryRoute

fun NavController.navigateGallery(memoUI: MemoUI, navOptions: NavOptions) {
    navigate(GalleryRoute(memoUI.toMemoString()), navOptions)
}

fun NavGraphBuilder.galleryNavGraph(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onBack: () -> Unit,
    onClickCamera: (MemoUI) -> Unit,
    onClickDone: (MemoUI) -> Unit,
) {
    composable<GalleryRoute> { navBackStackEntry ->
        val memo = navBackStackEntry.toRoute<Route.MemoCreate>().memo.toMemoUI()
        GalleryRoute(
            memoUI = memo,
            onBack = onBack,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onClickCamera = { onClickCamera(it) },
            onClickDone = { onClickDone(it) },
        )
    }
}