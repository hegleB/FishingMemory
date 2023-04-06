package com.qure.create.location

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.qure.core.BaseActivity
import com.qure.core.extensions.Empty
import com.qure.core.extensions.Spacing
import com.qure.core.extensions.getStringArrayCompat
import com.qure.core.util.setOnSingleClickListener
import com.qure.create.MemoCreateActivity
import com.qure.create.MemoCreateActivity.Companion.ARG_AREA
import com.qure.create.MemoCreateActivity.Companion.ARG_AREA_COORDS
import com.qure.create.R
import com.qure.create.databinding.ActivityLocationSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationSettingActivity :
    BaseActivity<ActivityLocationSettingBinding>(R.layout.activity_location_setting),
    RegionPositionCallback, AreaNameCallback{

    lateinit var listener: RegionPositionCallback
    lateinit var arealistener: AreaNameCallback

    private var areaName = String.Empty
    private var coords = String.Empty
    private var currentItemPosition = 0
    private lateinit var adapter: LocationSettingPagerAdapter
    private var selectedRegionName = MutableList(2, { String.Empty })
    private var selectedRegionId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener = this
        arealistener = this
        adapter = LocationSettingPagerAdapter(this@LocationSettingActivity, getFragments())
        initViewPager()
        initEvent()
    }

    private fun initViewPager() {
        binding.apply {
            viewPagerActivityLocationSetting.adapter = adapter
            viewPagerActivityLocationSetting.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            viewPagerActivityLocationSetting.isUserInputEnabled = false
        }
    }

    private fun getFragments(): MutableList<Fragment> {
        return mutableListOf(
            LocationSettingFragment.newInstance(
                title = getString(R.string.selection_do),
                subTitle = getString(R.string.do_name),
                regionArray = Region.getArray(this),
                regionName = String.Empty,
                listener = listener,
                arealistener = arealistener,
            ),
            LocationSettingFragment.newInstance(
                title = getString(R.string.selection_city),
                subTitle = getString(R.string.city_name),
                regionArray = Region.getArray(this),
                regionName = String.Empty,
                listener = listener,
                arealistener = arealistener,
            ),
            LocationSettingFragment.newInstance(
                title = getString(R.string.selection_map),
                subTitle = getString(R.string.map),
                regionArray = emptyArray(),
                regionName = String.Empty,
                listener = listener,
                arealistener = arealistener,
            )
        )
    }

    private fun initEvent() {
        setViewPagePosition()
        setButtonoPageTransition()
        closeLocationSetting()
    }

    private fun closeLocationSetting() {
        binding.imageViewActivityLocationSettingClose.setOnSingleClickListener {
            finish()
        }
    }

    private fun setViewPagePosition() {
        binding.apply {
            buttonActivityLocationSettingNext.setOnSingleClickListener {
                if (currentItemPosition == 2) {
                    val intent = Intent(this@LocationSettingActivity, MemoCreateActivity::class.java)
                    intent.putExtra(ARG_AREA, areaName)
                    intent.putExtra(ARG_AREA_COORDS, coords)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                viewPagerActivityLocationSetting.run {
                    currentItem += PAGE_INCREMENT_VALUE
                    currentItemPosition = currentItem
                }
            }
            buttonActivityLocationSettingPrevious.setOnSingleClickListener {
                viewPagerActivityLocationSetting.run {
                    currentItem -= PAGE_INCREMENT_VALUE
                    currentItemPosition = currentItem
                }
            }
        }
    }

    private fun setButtonoPageTransition() {
        binding.apply {
            viewPagerActivityLocationSetting.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when (position) {
                        0 -> setPreViousAndNextButton(position)
                        1 -> setPreViousAndNextButton(position)
                        else -> setPreViousAndNextButton(position)
                    }
                }
            })
        }
    }

    private fun setPreViousAndNextButton(position: Int) {
        with(binding) {
            buttonActivityLocationSettingPrevious.visibility =
                if (position == 0) View.INVISIBLE else View.VISIBLE
            buttonActivityLocationSettingNext.text =
                if (position == 2) getString(R.string.location_setting_setting) else
                    getString(R.string.location_setting_next)
            setEnabledNetxtButton(position)
        }
    }

    private fun setEnabledNetxtButton(position: Int) {
        when {
            selectedRegionName[0] != String.Empty && position == 0 -> {
                binding.buttonActivityLocationSettingNext.isEnabled = true
            }
            selectedRegionName[1] == String.Empty && position == 1 -> {
                binding.buttonActivityLocationSettingNext.isEnabled = false
            }
        }
    }


    override fun setRegionPosition(postion: Int) {
        binding.buttonActivityLocationSettingNext.isEnabled = true
        setSelectedRegionName(postion)
    }

    private fun setSelectedRegionName(postion: Int) {
        val regionArray = Region.getArray(this@LocationSettingActivity, postion)
        when (currentItemPosition) {
            0 -> {
                selectedRegionId = postion
                selectedRegionName[0] = getStringArrayCompat(R.array.array_region)[postion]
                selectedRegionName[1] = String.Empty
                refreshAdapter(regionArray)
            }
            1 -> {
                selectedRegionName[1] =
                    Region.getArray(this@LocationSettingActivity, selectedRegionId)[postion]
                refreshAdapter(regionArray)
            }
        }
    }

    private fun refreshAdapter(regionArray: Array<String>) {
        when (currentItemPosition) {
            0 -> setRefreshAdapter(regionArray, R.string.selection_city, R.string.city_name)
            else -> setRefreshAdapter(emptyArray(), R.string.selection_map, R.string.map)
        }
    }

    private fun setRefreshAdapter(regionArray: Array<String>, title: Int, subTitle: Int) {
        adapter.refreshFragment(
            currentItemPosition + 1,
            LocationSettingFragment.newInstance(
                title = getString(title),
                subTitle = getString(subTitle),
                regionArray = regionArray,
                regionName = selectedRegionName.joinToString(String.Spacing),
                listener = listener,
                arealistener = arealistener,
            )
        )
    }
    override fun setAreaName(name: String, coords: String) {
        this.areaName = name
        this.coords = coords
    }

    companion object {
        private const val PAGE_INCREMENT_VALUE = 1
        const val REQUEST_CODE_AREA = 0
    }

}