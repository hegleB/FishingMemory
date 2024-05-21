package com.qure.create.location

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.qure.core.BaseComposeActivity
import com.qure.core_design.compose.theme.FishingMemoryTheme
import com.qure.create.MemoCreateActivity
import com.qure.domain.ARG_AREA
import com.qure.domain.ARG_AREA_COORDS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationSettingActivity : BaseComposeActivity() {
    private val viewModel by viewModels<LocationSettingViewModel>()

    @Composable
    override fun Screen() {
        FishingMemoryTheme {
            LocationSettingScreen(
            LocationSettingRoute(
                viewModel = viewModel,
                onClickClose = { finish() },
                onClickNext = viewModel::onClickNext,
                onClickPrevious = viewModel::onClickPrevious,
                setReverseCoordsString = viewModel::getReverseGeocoding,
                setGeoconding = viewModel::getGeocoding,
                setReverseCoordsString = viewModel::fetchReverseGeocoding,
                setGeoconding = viewModel::fetchGeocoding,
                setLocation = { location, coords ->
                    val intent =
                        Intent(this@LocationSettingActivity, MemoCreateActivity::class.java)
                    intent.putExtra(ARG_AREA, location)
                    intent.putExtra(ARG_AREA_COORDS, coords)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                },
                setDoIndex = viewModel::setDoIndex,
                setCityIndex = viewModel::setCityIndex,
                setRegions = { viewModel.setRegions(it.toList()) },
                setSelectedRegions = { viewModel.setSelectedRegions(it.toList()) }
            )
        }
    }
}
