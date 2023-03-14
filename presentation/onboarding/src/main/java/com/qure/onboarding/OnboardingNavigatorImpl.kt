package com.qure.onboarding

import android.content.Context
import android.content.Intent
import com.qure.navigator.OnboardingNavigator
import javax.inject.Inject

class OnboardingNavigatorImpl @Inject constructor() : OnboardingNavigator {
    override fun intent(context: Context): Intent {
        return Intent(context, OnboardingActivity::class.java)
    }
}