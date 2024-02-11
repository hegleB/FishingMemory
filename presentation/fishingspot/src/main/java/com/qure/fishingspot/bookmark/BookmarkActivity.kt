package com.qure.fishingspot.bookmark

import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.qure.core.BaseComposeActivity
import com.qure.core_design.compose.theme.FishingMemoryTheme
import com.qure.domain.SPOT_DATA
import com.qure.navigator.FishingSpotNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BookmarkActivity : BaseComposeActivity() {
    @Inject
    lateinit var fishingSpotNavigator: FishingSpotNavigator

    private val viewModel by viewModels<BookmarkViewModel>()

    @Composable
    override fun Screen() {
        FishingMemoryTheme {
            BookmarkScreen(
                viewModel = viewModel,
                onBack = { finish() },
                navigateToFishingSpot = { fishingSpot ->
                    val intent = fishingSpotNavigator.intent(this)
                    intent.putExtra(SPOT_DATA, fishingSpot)
                    startActivity(intent)
                },
                onClickPhoneNumber = { phoneNumber ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:$phoneNumber"))
                    startActivity(intent)
                },
            )
        }
    }
}
