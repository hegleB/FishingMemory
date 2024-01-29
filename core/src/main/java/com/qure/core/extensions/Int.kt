package com.qure.core.extensions

import android.content.Context
import android.util.TypedValue

fun Int.twoDigitsFormat(): String {
    return "%02d".format(this)
}

fun Int.dpToPx(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics,
    ).toInt()
}
