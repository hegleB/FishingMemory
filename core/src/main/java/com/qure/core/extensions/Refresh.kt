package com.qure.core.extensions

import android.view.ViewGroup
import com.simform.refresh.SSPullToRefreshLayout

fun SSPullToRefreshLayout.initSwipeRefreshLayout() {
    setRefreshViewParams(
        ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            120
        )
    )
    setLottieAnimation("refresh.json")
    setRepeatMode(SSPullToRefreshLayout.RepeatMode.REPEAT)
    setRepeatCount(SSPullToRefreshLayout.RepeatCount.INFINITE)
    setRefreshStyle(SSPullToRefreshLayout.RefreshStyle.NORMAL)
}