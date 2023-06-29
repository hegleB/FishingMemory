package com.qure.login

import android.content.Context
import android.content.Intent
import com.qure.navigator.LoginNavigator
import javax.inject.Inject

class LoginNavigatorImpl @Inject constructor(): LoginNavigator {
    override fun intent(context: Context): Intent {
        return Intent(context, LoginActivity::class.java)
    }
}