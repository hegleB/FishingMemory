package com.qure.program_information

import androidx.compose.runtime.Composable
import com.qure.core.BaseComposeActivity
import com.qure.core.extensions.Empty
import com.qure.core_design.compose.theme.FishingMemoryTheme
import com.qure.domain.WEB_URL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProgramInformationActivity : BaseComposeActivity() {
    @Composable
    override fun Screen() {
        val webUrl = intent.getStringExtra(WEB_URL) ?: String.Empty
        FishingMemoryTheme {
            ProgramInformationRoute(
                webUrl = webUrl,
                onClose = { finish() },
            )
        }
    }
}
