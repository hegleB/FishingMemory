package com.qure.ui.extentions

import android.view.View
import androidx.core.view.isVisible

fun View.visiable() {
    this.isVisible = true
}

fun View.gone() {
    this.isVisible = false
}