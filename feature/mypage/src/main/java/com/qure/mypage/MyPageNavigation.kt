package com.qure.mypage

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.qure.navigation.MainTabRoute

fun NavController.navigationMyPage(navOptions: NavOptions) {
    navigate(MainTabRoute.MyPage, navOptions)
}

fun NavGraphBuilder.myPageNavGraph(
    padding: PaddingValues,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    navigateToBookmark: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToPolicyPrivacy: () -> Unit,
    navigateToPolicyService: () -> Unit,
    navigateToOpenSourceLicense: () -> Unit,
    navigateToDarkMode: () -> Unit,
) {
    composable<MainTabRoute.MyPage> {
        MyPageRoute(
            padding = padding,
            navigateToBookmark = navigateToBookmark,
            navigateToLogin = navigateToLogin,
            navigateToPolicyPrivacy = navigateToPolicyPrivacy,
            navigateToPolicyService = navigateToPolicyService,
            navigateToOpenSourceLicense = navigateToOpenSourceLicense,
            navigateToDarkMode =navigateToDarkMode,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}
