package com.qure.home.home

import android.content.Context
import android.content.Intent
import com.qure.home.MainActivity
import com.qure.navigator.HomeNavigator
import javax.inject.Inject

class HomeNavigatorImpl @Inject constructor() : HomeNavigator {
    override fun intent(context: Context): Intent {
        return Intent(context, MainActivity::class.java)
    }
}