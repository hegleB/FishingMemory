package com.qure.map

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.qure.model.FishingSpotUI
import com.qure.ui.model.MemoUI
import com.qure.navigation.Route.Map as MapRoute

fun NavController.navigateMap() {
    navigate(MapRoute)
}

fun NavGraphBuilder.mapNavGraph(
    onBack: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    navigateToDetailFishingSpot: (FishingSpotUI) -> Unit,
    navigateToDetailMemo: (MemoUI) -> Unit,
    onClickPhoneNumber: (String) -> Unit,
) {
    composable<MapRoute> {
        MapRoute(
            onBack = onBack,
            navigateToDetailFishingSpot = navigateToDetailFishingSpot,
            navigateToDetailMemo = navigateToDetailMemo,
            onClickPhoneNumber = onClickPhoneNumber,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}