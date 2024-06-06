package com.qure.login

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.qure.core.BaseComposeActivity
import com.qure.core_design.compose.theme.FishingMemoryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseComposeActivity() {
//    @Inject
//    lateinit var homeNavigator: HomeNavigator

    protected val viewModel by viewModels<LoginViewModel>()

    @Composable
    override fun Screen() {
        FishingMemoryTheme {
            LoginRoute(
                viewModel = viewModel,
                navigateToHome = {  },
            )
        }
    }
}
