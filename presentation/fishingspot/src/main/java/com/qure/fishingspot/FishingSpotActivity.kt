package com.qure.fishingspot

import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.qure.core.BaseComposeActivity
import com.qure.core_design.compose.theme.FishingMemoryTheme
import com.qure.domain.SPOT_DATA
import com.qure.model.FishingSpotUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FishingSpotActivity :
    BaseComposeActivity() {
    private val viewModel by viewModels<FishingSpotViewModel>()
    private var fishingSpot: FishingSpotUI = FishingSpotUI()

    @Composable
    override fun Screen() {
        initData()
        viewModel.checkBookmark(fishingSpot.number)
        FishingMemoryTheme {
            FishingSpotScreen(
                fishingSpot = fishingSpot,
                viewModel = viewModel,
                onBack = { finish() },
                onClickBookmark = { viewModel.toggleBookmarkButton(fishingSpot) },
                onClickPhoneNumber = {
                    val intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("tel:${fishingSpot.phone_number}"))
                    startActivity(intent)
                },
            )
        }
    }

    private fun initData() {
        fishingSpot = intent.getParcelableExtra(SPOT_DATA) ?: FishingSpotUI()
    }
}
