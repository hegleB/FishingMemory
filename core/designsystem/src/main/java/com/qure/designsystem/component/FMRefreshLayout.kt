package com.qure.designsystem.component

import android.content.Context
import android.content.res.Configuration
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.qure.core.designsystem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FMRefreshLayout(
    onRefresh: () -> Unit,
    isRefresh: Boolean,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val state = rememberPullToRefreshState()

    if (state.isRefreshing) {
        LaunchedEffect(true) {
            onRefresh()
        }
    }

    if (isRefresh.not()) {
        LaunchedEffect(true) {
            state.endRefresh()
        }
    }

    val animatedHeight by animateDpAsState(
        targetValue = if (isRefresh) {
            45.dp
        } else {
            lerp(
                0.dp,
                90.dp,
                state.progress.coerceIn(0f..1f),
            )
        },
        label = "",
    )

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .nestedScroll(state.nestedScrollConnection),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(animatedHeight)
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            ProgressIndicator(
                context = context,
                isPlaying = true,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
        content()
    }
}

@Composable
private fun ProgressIndicator(
    context: Context,
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
) {
    val currentNightMode =
        context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    val refreshId = if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
        R.raw.refresh
    } else {
        R.raw.refresh_night
    }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(refreshId))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        restartOnPlay = true,
        iterations = Int.MAX_VALUE,
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier.size(40.dp),
    )
}
