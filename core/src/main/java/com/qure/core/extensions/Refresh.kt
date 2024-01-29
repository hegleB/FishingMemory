package com.qure.core.extensions

import android.content.res.Configuration
import android.view.ViewGroup
import com.simform.refresh.SSPullToRefreshLayout

fun SSPullToRefreshLayout.initSwipeRefreshLayout() {
    setRefreshViewParams(
        ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            120,
        ),
    )
    val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
        setLottieAnimation("refresh.json")
    } else {
        setLottieAnimation("refresh_night.json")
    }
    setRepeatMode(SSPullToRefreshLayout.RepeatMode.REPEAT)
    setRepeatCount(SSPullToRefreshLayout.RepeatCount.INFINITE)
    setRefreshStyle(SSPullToRefreshLayout.RefreshStyle.NORMAL)
}
