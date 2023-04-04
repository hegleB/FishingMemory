package com.qure.core.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat

fun Context.getDrawableCompat(@DrawableRes drawableRes: Int): Drawable =
    requireNotNull(ContextCompat.getDrawable(this, drawableRes))

fun Context.getColorCompat(@ColorRes color: Int) =
    ContextCompat.getColor(this, color)

fun Context.getStringArrayCompat(@ArrayRes array: Int) =
    this.resources.getStringArray(array)