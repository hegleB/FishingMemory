package com.qure.create.location

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.qure.core.BaseActivity
import com.qure.core.util.setOnSingleClickListener
import com.qure.create.R
import com.qure.create.databinding.ActivityLocationSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationSettingActivity :
    BaseActivity<ActivityLocationSettingBinding>(R.layout.activity_location_setting),
    RegionPositionCallback {
    
    lateinit var listener: RegionPositionCallback

    private lateinit var adapter: LocationSettingPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener = this
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
                listener = listener
            ),
            LocationSettingFragment.newInstance(
                title = getString(R.string.selection_city),
                subTitle = getString(R.string.city_name),
                regionArray = Region.getArray(this),
                listener = listener
            ),
            LocationSettingFragment.newInstance(
                title = getString(R.string.selection_map),
                subTitle = getString(R.string.map),
                regionArray = emptyArray(),
                listener = listener
            )
        )
    }

    private fun initEvent() {
        setViewPagePosition()
        setButtonTextToPageTransition()
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
                viewPagerActivityLocationSetting.run {
                    currentItem += PAGE_INCREMENT_VALUE
                }
            }
            buttonActivityLocationSettingPrevious.setOnSingleClickListener {
                viewPagerActivityLocationSetting.run {
                    currentItem -= PAGE_INCREMENT_VALUE
                }
            }
        }
    }

    private fun setButtonTextToPageTransition() {
        binding.apply {
            viewPagerActivityLocationSetting.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when (position) {
                        0 -> {
                            buttonActivityLocationSettingPrevious.visibility = View.INVISIBLE
                            buttonActivityLocationSettingNext.text =
                                getString(R.string.location_setting_next)
                        }
                        1 -> {
                            buttonActivityLocationSettingPrevious.visibility = View.VISIBLE
                            buttonActivityLocationSettingNext.text =
                                getString(R.string.location_setting_next)
                        }
                        else -> {
                            buttonActivityLocationSettingPrevious.visibility = View.VISIBLE
                            buttonActivityLocationSettingNext.text =
                                getString(R.string.location_setting_setting)
                        }
                    }
                }
            })
        }
    }

    private inner class LocationSettingPagerAdapter(
        fragmentActivity: FragmentActivity,
        var fragments: MutableList<Fragment>,
    ) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int = PAGES_NUMBER

        fun refreshFragment(index: Int, fragment: Fragment) {
            fragments[index] = fragment
            notifyItemChanged(index)
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }

    companion object {
        private const val PAGES_NUMBER = 3
        private const val PAGE_INCREMENT_VALUE = 1
        private const val END_PAGE = 2
    }

    override fun setRegionPosition(postion: Int) {
        adapter.refreshFragment(
            1,
            LocationSettingFragment.newInstance(
                title = getString(R.string.selection_city),
                subTitle = getString(R.string.city_name),
                regionArray = Region.getArray(this, postion),
                listener = listener
            )
        )
    }
}