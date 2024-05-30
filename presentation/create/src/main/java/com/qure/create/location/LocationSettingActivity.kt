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
import com.qure.domain.EXTRA_REQUEST_CODE
import com.qure.domain.REQUEST_CODE_AREA
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationSettingActivity : BaseComposeActivity() {
    private val viewModel by viewModels<LocationSettingViewModel>()

    @Composable
    override fun Screen() {
        FishingMemoryTheme {
            LocationSettingRoute(
                viewModel = viewModel,
                onClickClose = { finish() },
                onClickNext = viewModel::onClickNext,
                onClickPrevious = viewModel::onClickPrevious,
                setReverseCoordsString = viewModel::fetchReverseGeocoding,
                setGeocoding = viewModel::fetchGeocoding,
                setLocation = { location, coords ->
                    val intent =
                        Intent(this@LocationSettingActivity, MemoCreateActivity::class.java).apply {
                            putExtra(EXTRA_REQUEST_CODE, REQUEST_CODE_AREA)
                            putExtra(ARG_AREA, location)
                            putExtra(ARG_AREA_COORDS, coords)
                        }
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                },
                setDoIndex = viewModel::setDoIndexDate,
                setCityIndex = viewModel::setCityIndexData,
                setRegions = { regions -> viewModel.setRegionsData(regions.toList()) },
                setSelectedRegions = { selectedRegions ->
                    viewModel.setSelectedRegionsData(selectedRegions.toList())
                }
            )
        }
    }
}
