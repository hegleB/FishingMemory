package com.qure.core_design.compose.components

import androidx.annotation.StringRes
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle

@Composable
fun FMButton(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit,
    textStyle: TextStyle = TextStyle(),
    textModifier: Modifier = Modifier,
    buttonColor: Color = Color.White,
    fontColor: Color = Color.Black,
    shape: Shape = CircleShape,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
        ),
        shape = shape,
    ) {
        Text(
            modifier = textModifier,
            text = text,
            style = textStyle,
            color = fontColor,
        )
    }
}
