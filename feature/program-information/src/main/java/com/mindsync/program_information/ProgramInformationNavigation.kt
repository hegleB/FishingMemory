package com.mindsync.program_information

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.qure.navigation.Route
import com.qure.navigation.Route.ProgramInformation as ProgramInformationRoute

fun NavController.navigateProgramInformation(url: String) {
    navigate(Route.ProgramInformation(url))
}

fun NavGraphBuilder.programInformationNavGraph(
    onBack: () -> Unit,
) {
    composable<ProgramInformationRoute> { navBackStackEntry ->
        val url = navBackStackEntry.toRoute<Route.ProgramInformation>().url
        ProgramInformationRoute(
            webUrl = url,
            onBack = onBack,
        )
    }
}