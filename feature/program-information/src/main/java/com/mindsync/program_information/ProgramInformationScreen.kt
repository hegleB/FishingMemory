package com.mindsync.program_information

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qure.designsystem.component.FMCloseButton
import com.qure.designsystem.component.FMWebView

@Composable
fun ProgramInformationRoute(
    webUrl: String = "",
    onBack: () -> Unit,
) {
    ProgramInformationScreen(
        webUrl = webUrl,
        onBack = onBack,
    )
}

@Composable
private fun ProgramInformationScreen(
    modifier: Modifier = Modifier,
    webUrl: String = "",
    onBack: () -> Unit = { },
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(70.dp),
        ) {
            FMCloseButton(
                modifier = Modifier
                    .size(25.dp)
                    .align(Alignment.CenterStart)
                    .offset(x = 20.dp),
                onClickClose = { onBack() },
                iconColor = MaterialTheme.colorScheme.onBackground,
            )
        }
        FMWebView(
            modifier = modifier
                .fillMaxSize(),
            webUrl = webUrl,
        )
    }
}

