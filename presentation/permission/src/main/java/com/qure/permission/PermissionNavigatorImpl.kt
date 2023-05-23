package com.qure.permission

import android.content.Context
import android.content.Intent
import com.qure.navigator.PermissionNavigator
import javax.inject.Inject

class PermissionNavigatorImpl @Inject constructor(): PermissionNavigator {
    override fun intent(context: Context): Intent {
        return Intent(context, PermissionActivity::class.java)
    }
}