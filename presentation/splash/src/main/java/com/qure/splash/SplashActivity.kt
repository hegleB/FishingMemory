package com.qure.splash

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.qure.core.BaseComposeActivity
import com.qure.core_design.compose.theme.FishingMemoryTheme
import com.qure.navigator.LoginNavigator
import com.qure.navigator.OnboardingNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseComposeActivity() {
    @Inject
    lateinit var onboardingNavigator: OnboardingNavigator

//    @Inject
//    lateinit var homeNavigator: HomeNavigator

    @Inject
    lateinit var loginNavigator: LoginNavigator

    private val viewModel by viewModels<SplashViewModel>()

    @Composable
    override fun Screen() {
        FishingMemoryTheme(
            darkTheme = false,
            dynamicColor = false,
        ) {
            SplashRoute(
                viewModel = viewModel,
                navigateToOnBoarding = { startActivity(onboardingNavigator.intent(this)); finish() },
                navigateToHome = {  },
                navigateToLogin = { startActivity(loginNavigator.intent(this)); finish() },
            )
        }
    }
}
