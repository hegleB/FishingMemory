package com.qure.fishingmemory.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.qure.fishingmemory.R
import com.qure.fishingmemory.base.BaseActivity
import com.qure.fishingmemory.databinding.ActivitySplashBinding
import com.qure.fishingmemory.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playAnimation()
        goToHomeOrSignupActivityWithDelay()
    }

    private fun goToHomeOrSignupActivityWithDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
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

    companion object {
        private const val DELAYED_MILLIS = 3500L
    }
}