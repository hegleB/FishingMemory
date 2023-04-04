package com.qure.create.location

import android.os.Bundle
import android.view.View
import com.qure.core.BaseFragment
import com.qure.core.extensions.Empty
import com.qure.core.extensions.Spacing
import com.qure.create.R
import com.qure.create.databinding.FragmentLocationSettingBinding
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"

@AndroidEntryPoint
class LocationSettingFragment(listener: RegionPositionCallback) :
    BaseFragment<FragmentLocationSettingBinding>(R.layout.fragment_location_setting) {

    private var title: String? = null
    private var subTitle: String? = null
    private var regionName: String? = null
    private var regionArray: Array<String> = emptyArray()

    private var listener: RegionPositionCallback

    private lateinit var adapter: LocationRegionAdapter

    init {
        this.listener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_PARAM1)
            subTitle = it.getString(ARG_PARAM2)
            regionName = it.getString((ARG_PARAM3))
            regionArray = it.getStringArray(ARG_PARAM4) as Array<String>
        }
        adapter = LocationRegionAdapter(regionArray)
        initView()
        initAdapter()

    }

    private fun isNotExistCityName() = regionName?.contains(NOT_EXIST_CITY_NAME) ?: false

    private fun initAdapter() {
        adapter.setItemClickListener(object : LocationRegionAdapter.ItemClickListener {
            override fun onClick(position: Int) {
                listener.setRegionPosition(position)
            }
        })
    }

    private fun initView() {
        binding.apply {
            textViewFragmentLocationSettingTitle.text = title
            textViewFragmentLocationSettingSubtitle.text = subTitle
            textViewFragmentLocationSettingSelectedName.text = if (isNotExistCityName())
                regionName?.split(String.Spacing)?.get(0) ?: String.Empty else regionName
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerViewFragmentLocationSetting.adapter = adapter
            recyclerViewFragmentLocationSetting.addItemDecoration(LocationItemDecoration(10))
        }
    }


    companion object {
        private const val NOT_EXIST_CITY_NAME = "없음"
        fun newInstance(
            title: String,
            subTitle: String,
            regionArray: Array<String>,
            regionName: String,
            listener: RegionPositionCallback,
        ) =
            LocationSettingFragment(listener).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, title)
                    putString(ARG_PARAM2, subTitle)
                    putString(ARG_PARAM3, regionName)
                    putStringArray(ARG_PARAM4, regionArray)
                }
            }
    }
}

interface RegionPositionCallback {
    fun setRegionPosition(postion: Int)
}
