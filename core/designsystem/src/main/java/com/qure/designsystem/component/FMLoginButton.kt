package com.qure.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qure.core.designsystem.R
import com.qure.designsystem.utils.FMPreview

@Composable
fun FMLoginButton(
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
        Image(
            painter = painterResource(id = R.drawable.ic_kakao_logo),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            modifier = textModifier,
            text = text,
            style = textStyle,
            color = fontColor,
        )
    }
}

@Preview
@Composable
private fun FMKakaoLoginButtonPreview() = FMPreview {
    FMLoginButton(
        modifier = Modifier.fillMaxWidth().heightIn(60.dp),
        text = "카카오로 계속하기",
        onClick = { },
        buttonColor = Color(0xFFFEE500),
        shape = RoundedCornerShape(15.dp),
        textStyle = MaterialTheme.typography.displayMedium,
    )
}
