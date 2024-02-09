package com.qure.core_design.compose.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.qure.core_design.R

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

//@Composable
//fun FMFavoriteButton(
//    modifier: Modifier = Modifier,
//    onClickFavorite: () -> Unit,
//    favoriteIcon: Painter,
//    iconColor: Color = Color.Unspecified,
//) {
//    val favoriteIcon = if (isFavorite) R.raw.start else R.raw.
//    IconButton(
//        modifier = modifier,
//        onClick = { onClickFavorite },
//    ) {
//        Icon(
//            painter = if (isFavorite) ,
//            contentDescription = null,
//            tint = iconColor,
//        )
//    }
//}
