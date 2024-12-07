package com.qure.fishingspot

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.qure.model.FishingSpotUI
import com.qure.navigation.Route
import com.qure.navigation.Route.FishingSpot as FishingSpotRoute

fun NavController.navigateFishingSpot(fishingSpot: FishingSpotUI) {
    navigate(Route.FishingSpot(fishingSpot))
}

fun NavGraphBuilder.fishingSpotNavGraph(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onBack: () -> Unit,
    onClickPhoneNumber: (String) -> Unit,
) {
    composable<FishingSpotRoute>(
        typeMap = FishingSpotRoute.typeMap,
    ) { navBackStackEntry ->
        val fishingSpot = navBackStackEntry.toRoute<Route.FishingSpot>().fishingSpot
        FishingSpotRoute(
            onBack = onBack,
            fishingSpot = fishingSpot,
            onClickPhoneNumber = onClickPhoneNumber,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}