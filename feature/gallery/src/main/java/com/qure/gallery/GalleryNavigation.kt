package com.qure.gallery

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.qure.navigation.Route
import com.qure.ui.model.MemoUI
import com.qure.ui.model.SnackBarMessageType
import com.qure.navigation.Route.Gallery as GalleryRoute

fun NavController.navigateGallery(memoUI: MemoUI, isEdit: Boolean, navOptions: NavOptions) {
    navigate(GalleryRoute(memoUI, isEdit), navOptions)
}

fun NavGraphBuilder.galleryNavGraph(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onBack: () -> Unit,
    onClickCamera: (MemoUI, Boolean) -> Unit,
    onClickDone: (MemoUI, Boolean) -> Unit,
    onShowMessageSnackBar: (messageType: SnackBarMessageType) -> Unit,
) {
    composable<GalleryRoute>(
        typeMap = GalleryRoute.typeMap,
    ) { navBackStackEntry ->
        val memo = navBackStackEntry.toRoute<Route.MemoCreate>().memo
        val isEdit = navBackStackEntry.toRoute<Route.MemoCreate>().isEdit

        GalleryRoute(
            memoUI = memo,
            onBack = onBack,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onClickCamera = { memoUI -> onClickCamera(memoUI, isEdit) },
            onClickDone = { memoUI -> onClickDone(memoUI, isEdit) },
            onShowMessageSnackBar = onShowMessageSnackBar,
        )
    }
}