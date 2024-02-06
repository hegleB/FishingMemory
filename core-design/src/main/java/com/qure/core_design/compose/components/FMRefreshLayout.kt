package com.qure.core_design.compose.components

import android.content.Context
import android.content.res.Configuration
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.qure.core_design.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FMRefreshLayout(
    onRefresh: () -> Unit,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    fun refresh() = refreshScope.launch {
        refreshing = true
        onRefresh()
        delay(500)
        refreshing = false
    }

    val state = rememberPullRefreshState(refreshing, ::refresh)
    val animatedHeight by animateDpAsState(
        targetValue = if (refreshing) {
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
            .background(color = Color.White)
            .pullRefresh(state),
    ) {
        Box(
            modifier = Modifier
                .background(color = Color.White)
                .fillMaxWidth()
                .height(animatedHeight),
        ) {
            ProgressIndicator(
                context = context,
                isPlaying = true,
                modifier = Modifier.align(Alignment.BottomCenter),
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
