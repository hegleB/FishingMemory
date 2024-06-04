package com.qure.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FMGlideImage(
    modifier: Modifier = Modifier,
    model: Comparable<*>,
    contentDescription: String?,
    contentScale: ContentScale = ContentScale.Crop,
) {
    GlideImage(
        modifier = modifier,
        model = model,
        contentDescription = contentDescription,
        contentScale = contentScale,
    )
}