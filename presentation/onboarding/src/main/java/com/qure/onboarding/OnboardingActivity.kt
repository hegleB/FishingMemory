package com.qure.onboarding

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.qure.core.BaseComposeActivity
import com.qure.core_design.compose.theme.FishingMemoryTheme
import com.qure.navigator.PermissionNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingActivity : BaseComposeActivity() {
    @Inject
    lateinit var permissionNavigator: PermissionNavigator

    private val viewModel by viewModels<OnboardingViewModel>()

    @Composable
    override fun Screen() {
        FishingMemoryTheme {
            OnboardingScreen(
                viewModel = viewModel,
                navigateToPermission = {
                    viewModel.writeFirstVisitor()
                    startActivity(permissionNavigator.intent(this@OnboardingActivity))
                    finish()
                },
            )
        }
    }
}