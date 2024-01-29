package com.qure.navigator

import android.content.Context
import android.content.Intent

interface Navigator {
    fun intent(context: Context): Intent
}
