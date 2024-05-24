package com.qure.permission

import androidx.compose.runtime.Composable
import com.qure.core.BaseComposeActivity
import com.qure.core_design.compose.theme.FishingMemoryTheme
import com.qure.navigator.LoginNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PermissionActivity : BaseComposeActivity() {
    @Inject
    lateinit var loginNavigator: LoginNavigator

    @Composable
    override fun Screen() {
        FishingMemoryTheme {
            PermissionRoute(navigateToLogin = {
                startActivity(loginNavigator.intent(this))
                finish()
            })
        }
    }
}
