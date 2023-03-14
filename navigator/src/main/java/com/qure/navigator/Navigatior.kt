package com.qure.navigator

import android.content.Context
import android.content.Intent

interface Navigatior {
    fun intent(context: Context): Intent
}