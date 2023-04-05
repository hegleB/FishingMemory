package com.qure.create.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.naver.maps.map.overlay.Marker
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons
import com.qure.core.BaseFragment
import com.qure.core.extensions.Empty
import com.qure.core.extensions.Spacing
import com.qure.core.extensions.getColorCompat
import com.qure.create.R
import com.qure.create.databinding.FragmentLocationSettingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"

@AndroidEntryPoint
class LocationSettingFragment(listener: RegionPositionCallback) :
    BaseFragment<FragmentLocationSettingBinding>(R.layout.fragment_location_setting),
    OnMapReadyCallback {

    private val viewModel by viewModels<LocationSettingViewModel>()

    private lateinit var naverMap: NaverMap

    private var title: String? = null
    private var subTitle: String? = null
    private var regionName: String? = null
    private var regionArray: Array<String> = emptyArray()

    private var listener: RegionPositionCallback

    private lateinit var adapter: LocationRegionAdapter

    init {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        arguments?.let {
            title = it.getString(ARG_PARAM1)
            subTitle = it.getString(ARG_PARAM2)
            regionName = it.getString(ARG_PARAM3)
            regionArray = it.getStringArray(ARG_PARAM4) as Array<String>
        }
        regionName = if (isNotExistCityName()) regionName?.split(String.Spacing)?.get(0)
            ?: String.Empty else regionName
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openMapFragment()
        initAdapter()
        initView()
    }

    private fun isNotExistCityName() = regionName?.contains(NOT_EXIST_CITY_NAME) ?: false

    private fun initAdapter() {
        adapter = LocationRegionAdapter(regionArray)
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
            textViewFragmentLocationSettingSelectedName.text = regionName
        }

        if (isMapPage()) {
            binding.recyclerViewFragmentLocationSetting.visibility = View.GONE
            binding.fragmentFragmentLocationSettingMap.visibility = View.VISIBLE
        } else {
            initRecyclerView()
            binding.recyclerViewFragmentLocationSetting.visibility = View.VISIBLE
            binding.fragmentFragmentLocationSettingMap.visibility = View.GONE
        }
    }

    private fun isMapPage() = subTitle == getString(R.string.map)

    private fun openMapFragment() {
        val fm = childFragmentManager
        val mapFragment =
            fm.findFragmentById(R.id.fragment_fragmentLocationSetting_map) as MapFragment?
                ?: MapFragment.newInstance().also {
                    fm.beginTransaction()
                        .add(R.id.fragment_fragmentLocationSetting_map, it)
                        .commit()
                }
        mapFragment.getMapAsync(this)
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerViewFragmentLocationSetting.adapter = adapter
            recyclerViewFragmentLocationSetting.addItemDecoration(LocationItemDecoration(10))
        }
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        moveMapCamera()
        initMapEvent()
    }

    private fun initMapEvent() {
        val marker = Marker()
        naverMap.setOnMapClickListener { point, coord ->
            marker.apply {
                position = LatLng(coord.latitude, coord.longitude)
                map = naverMap
                icon = MarkerIcons.BLACK
                iconTintColor = requireContext().getColorCompat(com.qure.core_design.R.color.blue_600)
            }
        }
    }

    private fun moveMapCamera() {
        viewModel.getGeocoding(regionName ?: String.Empty)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.UiState.collect {
                        val cameraUpdate = CameraUpdate.scrollTo(
                            LatLng(
                                it.geocodingUI?.get(0)?.y?.toDouble() ?: DEFAULT_LATITUDE,
                                it.geocodingUI?.get(0)?.x?.toDouble() ?: DEFAULT_LONGITUDE
                            )
                        )
                        naverMap.moveCamera(cameraUpdate)
                    }
                }
            }
        }
    }


    companion object {
        private const val NOT_EXIST_CITY_NAME = "없음"
        private const val DEFAULT_LATITUDE = 37.5666102
        private const val DEFAULT_LONGITUDE = 126.9783881
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
