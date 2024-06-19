package com.qure.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qure.core.ui.R

@Composable
fun OfflineView(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier
                    .size(80.dp),
                painter = painterResource(R.drawable.ic_wifi_off),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.internet_connection_instability_message),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp
            )
            Text(
                text = stringResource(R.string.network_check_message),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 15.sp
            )
        }
    }
}