package com.qure.splash

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qure.core.designsystem.R
import com.qure.designsystem.component.FMLottieAnimation
import com.qure.designsystem.theme.White
import com.qure.designsystem.utils.FMPreview
import com.qure.model.user.SplashState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SplashRoute(
    navigateToOnBoarding: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToMemoDetail: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {

    LaunchedEffect(viewModel.error) {
        viewModel.error.collectLatest(onShowErrorSnackBar)
    }

    val uiState by viewModel.splashUiState.collectAsStateWithLifecycle()

    SplashScreen(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White),
    )

    when (uiState) {
        is SplashUiState.Loading -> Unit
        is SplashUiState.Success -> {
            LaunchedEffect(Unit) {
                delay(3500)
                when ((uiState as SplashUiState.Success).onboardingState) {
                    SplashState.OPEN -> navigateToOnBoarding()
                    SplashState.SKIP -> navigateToLogin()
                    SplashState.NOT_LOGGED_IN -> navigateToLogin()
                    SplashState.LOGGED_IN -> navigateToHome()
                    SplashState.DEEPLINK -> navigateToMemoDetail()
                }
            }
        }
    }
}

@Composable
private fun SplashScreen(
    modifier: Modifier = Modifier,
) {
    FMLottieAnimation(
        modifier = modifier,
        lottieId = R.raw.splash_logo,
    )
}

@Preview
@Composable
private fun SplashScreenPreview() = FMPreview {
    SplashScreen()
}
