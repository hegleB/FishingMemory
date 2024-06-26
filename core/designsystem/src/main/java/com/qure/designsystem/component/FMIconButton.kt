package com.qure.designsystem.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.qure.core.designsystem.R
import com.qure.designsystem.utils.clickableWithoutRipple

@Composable
fun FMBackButton(
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit,
    iconColor: Color = Color.Unspecified,
) {
    IconButton(
        modifier = modifier,
        onClick = { onClickBack() },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = null,
            tint = iconColor,
        )
    }
}

@Composable
fun FMCircleAddButton(
    modifier: Modifier = Modifier,
    onClickAdd: () -> Unit,
    iconColor: Color = Color.Unspecified,
) {
    IconButton(
        modifier = modifier,
        onClick = { onClickAdd() },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_circle_add),
            contentDescription = null,
            tint = iconColor,
        )
    }
}

@Composable
fun FMBookmarkButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isBookmarkClicked: Boolean = false,
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.star),
    )

    val initialProgress = if (isBookmarkClicked) 0.4f else 0f
    val animatableProgress = remember { Animatable(initialProgress) }

    LaunchedEffect(isBookmarkClicked) {
        val targetValue = if (isBookmarkClicked) 0.6f else 0f
        animatableProgress.animateTo(
            targetValue = targetValue,
            animationSpec = tween(durationMillis = if (isBookmarkClicked) 1000 else 700),
        )
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickableWithoutRipple { onClick() },
    ) {
        LottieAnimation(
            composition = composition,
            contentScale = ContentScale.FillHeight,
            progress = { animatableProgress.value },
        )
    }
}

@Composable
fun FMMoreButton(
    modifier: Modifier = Modifier,
    onClickMore: () -> Unit,
    iconColor: Color = MaterialTheme.colorScheme.onBackground,
) {
    IconButton(
        modifier = modifier,
        onClick = { onClickMore() },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_more_vert),
            contentDescription = null,
            tint = iconColor,
        )
    }
}

@Composable
fun FMCloseButton(
    modifier: Modifier = Modifier,
    onClickClose: () -> Unit,
    iconColor: Color = MaterialTheme.colorScheme.background,
) {
    IconButton(
        modifier = modifier,
        onClick = { onClickClose() },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = null,
            tint = iconColor,
        )
    }
}

@Composable
fun FMMapButton(
    modifier: Modifier = Modifier,
    onClickMap: () -> Unit,
    iconColor: Color = MaterialTheme.colorScheme.background,
) {
    IconButton(
        modifier = modifier,
        onClick = { onClickMap() },
    ) {
        Icon(
            modifier = Modifier
                .padding(2.dp),
            painter = painterResource(id = R.drawable.ic_map),
            contentDescription = null,
            tint = iconColor,
        )
    }
}
