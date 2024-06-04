package com.qure.model.extensions

import android.content.Intent
import android.os.Build

fun <T> Intent.getParcelableExtraData(key: String, classType: Class<T>) =
    if (Build.VERSION.SDK_INT >= 33) {
        this.getParcelableExtra(key, classType)
    } else {
        @Suppress("DEPRECATION")
        this.getParcelableExtra(key)
    }
