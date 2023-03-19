package com.qure.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.qure.core.BaseActivity
import com.qure.navigator.HomeNavigator
import com.qure.navigator.OnboardingNavigator
import com.qure.navigator.LoginNavigator
import com.qure.splash.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    @Inject
    lateinit var onboardingNavigator: OnboardingNavigator

    @Inject
    lateinit var homeNavigator: HomeNavigator

    @Inject
    lateinit var loginNavigator: LoginNavigator

    private val viewModel by viewModels<SplashViewModel>()

    private var isFirstVisitor = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playAnimation()
        goToHomeOrSignupActivityWithDelay()
        observeViewModel()
    }

    private fun goToHomeOrSignupActivityWithDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = if (isFirstVisitor) {
                onboardingNavigator.intent(this)
            } else if (viewModel.isSignedUp()) {
                homeNavigator.intent(this)
            } else {
                loginNavigator.intent(this)
            }
            startActivity(intent)
            finish()
        }, DELAYED_MILLIS)
    }

    private fun playAnimation() {
        binding.lottieAnimationViewSplashActivityLogo.apply {
            layoutParams.width = (width * 0.61).toInt() // 220/360 = 0.611
            layoutParams.height = layoutParams.width
            setAnimation(R.raw.splash_logo)
            playAnimation()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.isFirstVisitor.collect {
                isFirstVisitor = it
            }
        }
    }

    companion object {
        private const val DELAYED_MILLIS = 3500L
    }
}