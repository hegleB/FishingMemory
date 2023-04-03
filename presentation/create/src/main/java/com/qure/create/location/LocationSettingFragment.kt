package com.qure.create.location

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.qure.core.BaseFragment
import com.qure.create.R
import com.qure.create.databinding.FragmentLocationSettingBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

@AndroidEntryPoint
class LocationSettingFragment(listener: RegionPositionCallback) :
    BaseFragment<FragmentLocationSettingBinding>(R.layout.fragment_location_setting) {

    private var title: String? = null
    private var subTitle: String? = null
    private var regionArray: Array<String> = emptyArray()

    private var listener: RegionPositionCallback

    private lateinit var adapter : LocationRegionAdapter

    init {
        this.listener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_PARAM1)
            subTitle = it.getString(ARG_PARAM2)
            regionArray = it.getStringArray(ARG_PARAM3) as Array<String>
        }
        adapter = LocationRegionAdapter(regionArray)
        initView()
        initAdapter()

    }

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
        fun newInstance(title: String, subTitle: String, regionArray: Array<String>, listener: RegionPositionCallback) =
            LocationSettingFragment(listener).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, title)
                    putString(ARG_PARAM2, subTitle)
                    putStringArray(ARG_PARAM3, regionArray)
                }
            }
    }
}

interface RegionPositionCallback {
    fun setRegionPosition(postion: Int)
}
