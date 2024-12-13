package com.qure.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qure.designsystem.utils.FMPreview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PolaroidLayout(
    modifier: Modifier = Modifier,
    itemCount: Int,
    content: @Composable (Int) -> Unit,
) {
    LazyLayout(
        modifier = modifier,
        itemProvider = {
            object : LazyLayoutItemProvider {
                override val itemCount: Int
                    get() = itemCount

                @Composable
                override fun Item(index: Int, key: Any) {
                    content(index)
                }
            }
        },
        measurePolicy = { constraints ->
            val placebleList = mutableListOf<Placeable>()
            val placeables = (0 until itemCount).map { index ->
                val m = measure(index, constraints)
                if (m.isNotEmpty()) {
                    placebleList.add(m[0])
                }
            }

            val maxWidth = constraints.maxWidth
            val cardWidth = (maxWidth * 0.8).toInt()
            val cardHeight = (cardWidth * 1.2).toInt()
            val overlapOffset = (cardHeight * 0.65).toInt()
            val totalHeight = cardHeight + (placeables.size - 1) * overlapOffset

            var yOffset = 0
            val xOffsetArray = IntArray(placeables.size)
            val yOffsetArray = IntArray(placeables.size)
            placebleList.forEachIndexed { index, placeable ->
                val xOffset = if (index % 2 == 0) {
                    (maxWidth - placeable.width) / 2 - (cardWidth * 0.2).toInt()
                } else {
                    (maxWidth - placeable.width) / 2 + (cardWidth * 0.2).toInt()
                }
                xOffsetArray[index] = xOffset
                yOffsetArray[index] = yOffset
                yOffset += overlapOffset
            }

            layout(maxWidth, totalHeight) {
                placebleList.forEachIndexed { index, placeable ->
                    placeable.placeRelative(
                        x = xOffsetArray[index],
                        y = yOffsetArray[index]
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun PolaroidLayoutPreview() = FMPreview {
    PolaroidLayout(
        modifier = Modifier,
        itemCount = 10,
    ) { index ->
        CardFront(
            modifier = Modifier
                .size(250.dp)
                .graphicsLayer {
                    rotationZ = if (index % 2 == 0) -15f else 15f
                },
        )
    }
}