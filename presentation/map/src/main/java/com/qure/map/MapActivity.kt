package com.qure.map

import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.qure.core.BaseComposeActivity
import com.qure.core_design.compose.theme.FishingMemoryTheme
import com.qure.domain.MEMO_DATA
import com.qure.domain.SPOT_DATA
import com.qure.navigator.DetailMemoNavigator
import com.qure.navigator.FishingSpotNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MapActivity : BaseComposeActivity() {
    @Inject
    lateinit var detailMemoNavigator: DetailMemoNavigator

    @Inject
    lateinit var fishingSpotNavigator: FishingSpotNavigator

    private val viewModel by viewModels<MapViewModel>()

    @Composable
    override fun Screen() {
        FishingMemoryTheme {
            MapRoute(
                viewModel = viewModel,
                onBack = { finish() },
                navigateToDetailFishingSpot = { fishingSpot ->
                    val intent = fishingSpotNavigator.intent(this).apply {
                        putExtra(SPOT_DATA, fishingSpot)
                    }
                    startActivity(intent)
                },
                navigateToDetailMemo = { memo ->
                    val intent = detailMemoNavigator.intent(this).apply {
                        putExtra(MEMO_DATA, memo)
                    }
                    startActivity(intent)
                },
                onClickPhoneNumber = { phoneNumber ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:$phoneNumber"))
                    startActivity(intent)
                }
            )
        }
    }
}
