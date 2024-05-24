package com.qure.program_information

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
import com.qure.core_design.compose.components.FMCloseButton
import com.qure.core_design.compose.components.FMWebView

@Composable
fun ProgramInformationRoute(
    webUrl: String = "",
    onClose: () -> Unit,
) {
    ProgramInformationContent(
        webUrl = webUrl,
        onClose = onClose,
    )
}

@Composable
private fun ProgramInformationContent(
    modifier: Modifier = Modifier,
    webUrl: String = "",
    onClose: () -> Unit = { },
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
                onClickClose = { onClose() },
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

