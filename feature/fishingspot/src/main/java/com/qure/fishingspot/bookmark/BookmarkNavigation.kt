package com.qure.create.location

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.qure.fishingspot.bookmark.BookmarkRoute
import com.qure.model.FishingSpotUI
import com.qure.navigation.Route.Bookmark as BookmarkRoute

fun NavController.navigateBookmark() {
    navigate(BookmarkRoute)
}

fun NavGraphBuilder.bookmarkNavGraph(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onBack: () -> Unit,
    navigateToFishingSpot: (FishingSpotUI) -> Unit,
    onClickPhoneNumber: (String) -> Unit,
) {
    composable<BookmarkRoute> {
        BookmarkRoute(
            onBack = onBack,
            navigateToFishingSpot = navigateToFishingSpot,
            onClickPhoneNumber = onClickPhoneNumber,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}