package com.qure.core.util

import android.os.SystemClock
import android.view.View

class OnSingleClickListener(
    private val interval: Int,
    private val onSingleClick: (View) -> Unit
) : View.OnClickListener {

    private var lastClickedTime: Long = 0L

    override fun onClick(v: View) {
        val elapsedRealTime = SystemClock.elapsedRealtime()
        if ((elapsedRealTime - lastClickedTime) < interval) {
            return
        }
        lastClickedTime = elapsedRealTime
        onSingleClick(v)
    }
}

fun View.setOnSingleClickListener(
    interval: Int = 200,
    onClick: (View) -> Unit = {}
) {
    setOnClickListener(OnSingleClickListener(interval, onClick))
}