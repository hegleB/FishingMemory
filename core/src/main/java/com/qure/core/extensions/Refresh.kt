package com.qure.core.extensions

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.simform.refresh.SSPullToRefreshLayout

fun SSPullToRefreshLayout.initSwipeRefreshLayout() {
    setRefreshViewParams(
        ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            120
        )
    )
    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
        setLottieAnimation("refresh.json")
    } else {
        setLottieAnimation("refresh_night.json")
    }
    setRepeatMode(SSPullToRefreshLayout.RepeatMode.REPEAT)
    setRepeatCount(SSPullToRefreshLayout.RepeatCount.INFINITE)
    setRefreshStyle(SSPullToRefreshLayout.RefreshStyle.NORMAL)
}