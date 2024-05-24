package com.qure.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qure.core_design.compose.components.FMLottieAnimation
import com.qure.core_design.compose.utils.FMPreview
import kotlinx.coroutines.delay

@Composable
fun SplashRoute(
    viewModel: SplashViewModel,
    navigateToOnBoarding: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    val isFirstVisitor by viewModel.isFirstVisitor.collectAsStateWithLifecycle()
    SplashContent(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
    )
    LaunchedEffect(Unit) {
        delay(3500)
        when {
            isFirstVisitor -> navigateToOnBoarding()
            viewModel.isSignedUp() -> navigateToHome()
            else -> navigateToLogin()
        }
    }
}

@Composable
private fun SplashContent(
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
    SplashContent()
}
