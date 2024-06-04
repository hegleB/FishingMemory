package com.qure.designsystem.component

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun FMLottieAnimation(
    modifier: Modifier = Modifier,
    @RawRes lottieId: Int,
    isPlaying: Boolean = true,
    restartOnPlay: Boolean = true,
    iterations: Int = Int.MAX_VALUE,
) {
    Box(
        modifier = modifier,
    ) {
        val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieId))
        val progress by animateLottieCompositionAsState(
            composition = lottieComposition,
            isPlaying = isPlaying,
            restartOnPlay = restartOnPlay,
            iterations = iterations,
        )
        LottieAnimation(
            modifier = modifier,
            composition = lottieComposition,
            progress = { progress },
        )
    }
}
