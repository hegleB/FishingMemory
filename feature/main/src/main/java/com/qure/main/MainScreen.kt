package com.qure.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.mindsync.program_information.programInformationNavGraph
import com.qure.camera.cameraNavHost
import com.qure.create.location.bookmarkNavGraph
import com.qure.create.location.locationSettingNavGraph
import com.qure.create.memoCreateNavGraph
import com.qure.designsystem.theme.Blue600
import com.qure.feature.main.R
import com.qure.fishingspot.fishingSpotNavGraph
import com.qure.gallery.galleryNavGraph
import com.qure.history.historyNavGraph
import com.qure.history.navigationHistory
import com.qure.home.homeNavGraph
import com.qure.login.loginNavGraph
import com.qure.map.mapNavGraph
import com.qure.memo.detail.memoDetailNavGraph
import com.qure.memo.memoListNavGraph
import com.qure.mypage.darkmode.darkModeNavGraph
import com.qure.mypage.myPageNavGraph
import com.qure.navigation.MainTabRoute
import com.qure.navigation.Route
import com.qure.onboarding.onboardingNavGraph
import com.qure.permission.permissionNavGraph
import com.qure.splash.splashNavGraph
import com.qure.ui.model.MemoUI
import kotlinx.coroutines.launch
import java.net.UnknownHostException

@Composable
fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator(),
    isKakaoOpenDeepLink: Boolean = false,
    memo: MemoUI = MemoUI(),
    setRoute: (Route) -> Unit = { },
    route: Route = Route.Splash,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val localContextResource = LocalContext.current.resources
    val onShowErrorSnackBar: (throwable: Throwable?) -> Unit = { throwable ->
        coroutineScope.launch {
            throwable?.message?.let {
                println("SnackBarMessage :  $it$")
                snackBarHostState.showSnackbar(
                    when (throwable) {
                        is UnknownHostException -> localContextResource.getString(R.string.error_message_network)
                        else -> localContextResource.getString(R.string.error_message_unknown)
                    }
                )
            }
        }
    }

    Scaffold(
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                NavHost(
                    navController = navigator.navController,
                    startDestination = navigator.startDestination,
                ) {
                    homeNavGraph(
                        padding = padding,
                        navigateToMemoList = navigator::navigateToMemoList,
                        navigateToDetailMemo = { memo ->
                            navigator.navigateToMemoDetail(
                                memoUI = memo,
                                navOptions = navOptions {
                                    popUpTo(MainTab.HOME.route) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            )
                        },
                        navigateToMap = navigator::navigateToMap,
                        onShowErrorSnackBar = onShowErrorSnackBar,
                    )
                    historyNavGraph(
                        padding = padding,
                        navigateToMap = navigator::navigateToMap,
                        navigateToMemoDetail = { memo ->
                            navigator.navigateToMemoDetail(
                                memoUI = memo,
                            )
                        },
                        navigateToMemoCreate = {
                            setRoute(MainTabRoute.History)
                            navigator.navigateToMemoCreate()
                        },
                        onShowErrorSnackBar = onShowErrorSnackBar,
                    )
                    myPageNavGraph(
                        padding = padding,
                        onShowErrorSnackBar = onShowErrorSnackBar,
                        navigateToBookmark = navigator::navigateToBookmark,
                        navigateToLogin = {
                            navigator.navigateToLogin(
                                navOptions = navOptions {
                                    popUpTo(MainTabRoute.Home) {
                                        inclusive = true
                                    }
                                }
                            )
                        },
                        navigateToPolicyPrivacy = {
                            navigator.navigateToProgramInformation(
                                localContextResource.getString(R.string.policy_privacy_url)
                            )
                        },
                        navigateToPolicyService = {
                            navigator.navigateToProgramInformation(
                                localContextResource.getString(R.string.policy_service_url)
                            )
                        },
                        navigateToOpenSourceLicense = {
                            navigator.navigateToProgramInformation(
                                localContextResource.getString(R.string.open_source_license_url)
                            )
                        },
                        navigateToDarkMode = navigator::navigateToDarkMode,
                    )

                    splashNavGraph(
                        navigateToHome = {
                            navigator.navigateToHome(
                                navOptions = navOptions {
                                    popUpTo(Route.Splash) {
                                        inclusive = true
                                    }
                                }
                            )
                        },
                        navigateToLogin = {
                            navigator.navigateToLogin(
                                navOptions = navOptions {
                                    popUpTo(Route.Splash) {
                                        inclusive = true
                                    }
                                }
                            )
                        },
                        navigateToMemoDetail = {
                            navigator.navigateToMemoDetail(
                                memoUI = memo,
                                navOptions {
                                    popUpTo(MainTabRoute.Home)
                                }
                            )
                        },
                        navigateToOnBoarding = navigator::navigateToOnboarding,
                        onShowErrorSnackBar = onShowErrorSnackBar,
                    )

                    onboardingNavGraph(
                        navigateToPermission = navigator::navigateToPermission,
                        onShowErrorSnackBar = onShowErrorSnackBar,
                    )

                    permissionNavGraph(
                        navigateToLogin = {
                            navigator.navigateToLogin(
                                navOptions = navOptions {
                                    popUpTo(Route.Splash) {
                                        inclusive = true
                                    }
                                }
                            )
                        },
                        onShowErrorSnackBar = onShowErrorSnackBar
                    )

                    loginNavGraph(
                        navigateToHome = {
                            navigator.navigateToHome(
                                navOptions = navOptions {
                                    popUpTo(Route.Login) {
                                        inclusive = true
                                    }
                                }
                            )
                        },
                        onShowErrorSnackBar = onShowErrorSnackBar
                    )

                    memoCreateNavGraph(
                        onBack = {
                            when (route) {
                                MainTabRoute.History -> {
                                    navigator.navController.navigationHistory(
                                        navOptions {
                                            popUpTo(MainTabRoute.History) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    )
                                }

                                Route.MemoList -> {
                                    navigator.navigateToMemoList(
                                        navOptions {
                                            popUpTo(Route.MemoList) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    )
                                }

                                else -> navigator.popBackStack()
                            }
                        },
                        navigateToLocationSetting = { memo ->
                            navigator.navigateToLocationSetting(
                                memoUI = memo,
                            )
                        },
                        navigateToGallery = { memo ->
                            navigator.navigateToGallery(
                                memoUI = memo,
                            )
                        },
                        navigateToMemoDetail = { memo ->
                            navigator.navigateToMemoDetail(
                                memoUI = memo,
                            )
                        },
                        onShowErrorSnackBar = onShowErrorSnackBar,
                    )

                    locationSettingNavGraph(
                        onBack = navigator::popBackStack,
                        navigateToMemoCreate = { memo ->
                            navigator.navigateToMemoCreate(
                                memoUI = memo,
                                isEdit = true,
                            )
                        },
                        onShowErrorSnackBar = onShowErrorSnackBar,
                    )

                    fishingSpotNavGraph(
                        onBack = navigator::popBackStack,
                        onClickPhoneNumber = { phoneNumber -> showPhoneCall(context, phoneNumber) },
                        onShowErrorSnackBar = onShowErrorSnackBar,
                    )

                    bookmarkNavGraph(
                        onBack = navigator::popBackStack,
                        navigateToFishingSpot = navigator::navigateToFishingSpot,
                        onClickPhoneNumber = { phoneNumber -> showPhoneCall(context, phoneNumber) },
                        onShowErrorSnackBar = onShowErrorSnackBar,
                    )

                    galleryNavGraph(
                        onBack = navigator::popBackStack,
                        onClickCamera = { memo ->
                            navigator.navigateToCamera(
                                memo = memo,
                                isEdit = true,
                            )
                        },
                        onClickDone = { memo ->
                            navigator.navigateToMemoCreate(
                                memoUI = memo,
                                isEdit = true,
                            )
                        },
                        onShowErrorSnackBar = onShowErrorSnackBar,
                    )

                    mapNavGraph(
                        onBack = navigator::popBackStack,
                        navigateToDetailFishingSpot = navigator::navigateToFishingSpot,
                        navigateToDetailMemo = { memo ->
                            setRoute(Route.Map)
                            navigator.navigateToMemoDetail(
                                memoUI = memo,
                            )
                        },
                        onClickPhoneNumber = { phoneNumber -> showPhoneCall(context, phoneNumber) },
                        onShowErrorSnackBar = onShowErrorSnackBar,
                    )

                    memoDetailNavGraph(
                        onBack = {
                            when (route) {
                                MainTabRoute.History -> {
                                    navigator.navController.navigationHistory(
                                        navOptions {
                                            popUpTo(MainTabRoute.History) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    )
                                }

                                Route.MemoList -> {
                                    navigator.navigateToMemoList(
                                        navOptions {
                                            popUpTo(Route.MemoList) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    )
                                }

                                MainTab.HOME.route -> {
                                    navigator.navigateToHome(
                                        navOptions {
                                            popUpTo(Route.Splash) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    )
                                }

                                else -> navigator.popBackStack()
                            }
                        },
                        onClickEdit = { memo ->
                            navigator.navigateToMemoCreate(
                                memoUI = memo,
                                isEdit = true,
                            )
                        },
                        onShowErrorSnackBar = onShowErrorSnackBar,
                    )

                    memoListNavGraph(
                        onBack = {
                            navigator.navigateToHome(
                                navOptions {
                                    popUpTo(Route.MemoList) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            )
                        },
                        navigateToMemoCreate = {
                            setRoute(Route.MemoList)
                            navigator.navigateToMemoCreate()
                        },
                        navigateToMemoDetail = { memo ->
                            navigator.navigateToMemoDetail(
                                memoUI = memo,
                                navOptions = navOptions {
                                    popUpTo(Route.MemoList) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            )
                        },
                        onShowErrorSnackBar = onShowErrorSnackBar,
                    )

                    darkModeNavGraph(
                        onBack = navigator::popBackStack,
                        onShowErrorSnackBar = onShowErrorSnackBar,
                    )

                    programInformationNavGraph(
                        onBack = navigator::popBackStack,
                        onShowErrorSnackBar = onShowErrorSnackBar,
                    )

                    cameraNavHost(
                        onShowErrorSnackBar = onShowErrorSnackBar,
                        navigateToMemoCreate = { memo ->
                            navigator.navigateToMemoCreate(
                                memoUI = memo,
                                isEdit = true,
                            )
                        },
                    )
                }
            }
        },
        bottomBar = {
            MainBottomBar(
                visible = navigator.shouldShowBottomBar(),
                tabs = MainTab.entries.toList(),
                currentTab = navigator.currentTab,
                onTabSelected = {
                    navigator.navigate(it)
                }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    )
}

private fun showPhoneCall(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("tel:$phoneNumber")
    }
    context.startActivity(intent)
}

@Composable
private fun MainBottomBar(
    visible: Boolean,
    tabs: List<MainTab>,
    currentTab: MainTab?,
    onTabSelected: (MainTab) -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideIn { IntOffset(0, it.height) },
        exit = fadeOut() + slideOut { IntOffset(0, it.height) }
    ) {
        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                )
                .padding(horizontal = 28.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            tabs.forEach { tab ->
                MainBottomBarItem(
                    tab = tab,
                    selected = tab == currentTab,
                    onClick = { onTabSelected(tab) },
                )
            }
        }
    }
}

@Composable
private fun RowScope.MainBottomBarItem(
    tab: MainTab,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .selectable(
                selected = selected,
                indication = null,
                role = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(tab.iconRes),
            contentDescription = tab.contentDescription,
            tint = if (selected) {
                Blue600
            } else {
                MaterialTheme.colorScheme.outline
            },
            modifier = Modifier.size(24.dp),
        )

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            text = tab.contentDescription,
            color = if (selected) Blue600 else MaterialTheme.colorScheme.onBackground,
        )
    }
}