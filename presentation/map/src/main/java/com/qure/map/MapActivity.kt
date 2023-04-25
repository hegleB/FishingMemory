package com.qure.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.PointF
import android.location.Location
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.NaverMap.OnMapClickListener
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.qure.core.BaseActivity
import com.qure.core.extensions.Comma
import com.qure.core.extensions.dpToPx
import com.qure.core.util.setOnSingleClickListener
import com.qure.map.databinding.ActivityMapBinding
import com.qure.memo.MemoListAdapter
import com.qure.memo.MemoListViewModel
import com.qure.memo.detail.DetailMemoActivity.Companion.MEMO_DATA
import com.qure.memo.model.MemoUI
import com.qure.navigator.DetailMemoNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MapActivity : BaseActivity<ActivityMapBinding>(R.layout.activity_map), OnMapReadyCallback {

    @Inject
    lateinit var detailMemoNavigator: DetailMemoNavigator

    private val memoViewModel by viewModels<MemoListViewModel>()
    private val adatper: MemoListAdapter by lazy {
        MemoListAdapter({
            val intent = detailMemoNavigator.intent(this)
            intent.putExtra(MEMO_DATA, it)
            startActivity(intent)
        })
    }
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        initView()
        initRecyclerView()
        openMapFragment()
        observe()
    }

    private fun initView() {
        binding.imageViewActivityMapBack.setOnSingleClickListener {
            finish()
        }

        binding.imageViewActivityMapLocation.setOnClickListener {
            updateCurrentLocation()
        }

        binding.chipGroupActivityMap.setOnCheckedChangeListener { group, checkedId ->
            selectMapType(checkedId)
        }
    }

    private fun initRecyclerView() {
        binding.bottomSheetActivityMap.recyclerViewBottomSheetMemoList.adapter = adatper
    }

    private fun selectMapType(checkedId: Int) {
        when (checkedId) {
            R.id.chip_activityMap_basic_map -> naverMap.mapType = NaverMap.MapType.Basic
            R.id.chip_activityMap_satellite_map -> naverMap.mapType = NaverMap.MapType.Satellite
            R.id.chip_activityMap_terrain_map -> naverMap.mapType = NaverMap.MapType.Terrain
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    memoViewModel.uiState.collect { uiState ->
                        val memos = uiState.filteredMemo
                        createMarkers(memos)
                        setBottomSheetView(memos)
                    }
                }
            }
        }
    }

    private fun createMarkers(memos: List<MemoUI>) {
        for (memo in memos) {
            val (lat, lng) = memo.coords.split(String.Comma).map { it.toDouble() }
            val latLng = LatLng(lat, lng)
            val marker = Marker()
            marker.apply {
                position = latLng
                map = naverMap
                icon = OverlayImage.fromResource(com.qure.core_design.R.drawable.bg_map_fill_marker)
                width = 100
                height = 120

                setOnClickListener {
                    val cameraUpdate = CameraUpdate.scrollTo(latLng)
                    val selectedMemos = memos.filter { it.coords == "${lat},${lng}" }
                    naverMap.moveCamera(cameraUpdate)
                    setBottomSheetView(selectedMemos)
                    true
                }
            }
        }
    }

    private fun setBottomSheetView(memos: List<MemoUI>) {
        binding.bottomSheetActivityMap.textViewBottomSheetMemoListCount.text = "${memos.size}개의 메모"
        adatper.submitList(memos)
    }

    private fun openMapFragment() {
        val fm = supportFragmentManager
        val mapFragment =
            fm.findFragmentById(R.id.fragment_activityMap) as MapFragment?
                ?: MapFragment.newInstance().also {
                    fm.beginTransaction()
                        .add(R.id.fragment_activityMap, it)
                        .commit()
                }
        mapFragment.getMapAsync(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        ) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            } else {
                onMapReady(naverMap)
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        setMapSettings()
        updateCurrentLocation()
        memoViewModel.getFilteredMemo()
    }

    private fun setMapSettings() {
        naverMap.apply {
            locationSource = locationSource
            locationTrackingMode = LocationTrackingMode.NoFollow
        }

        val uiSettings = naverMap.uiSettings
        uiSettings.apply {
            isCompassEnabled = false
            isZoomControlEnabled = false
            setLogoMargin(0, 0, 0, 60.dpToPx(this@MapActivity))
        }
    }

    private fun updateCurrentLocation() {
        var currentLocation: Location?
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                currentLocation = location
                naverMap.locationOverlay.run {
                    isVisible = true
                    position = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                }
                val cameraUpdate = CameraUpdate.scrollTo(
                    LatLng(
                        currentLocation!!.latitude,
                        currentLocation!!.longitude
                    )
                )
                naverMap.moveCamera(cameraUpdate)
            }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}