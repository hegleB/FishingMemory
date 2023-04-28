package com.qure.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PointF
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.NaverMap.OnMapClickListener
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.qure.core.BaseActivity
import com.qure.core.extensions.dpToPx
import com.qure.core.extensions.toReverseCoordsString
import com.qure.core.util.setOnSingleClickListener
import com.qure.map.databinding.ActivityMapBinding
import com.qure.memo.MemoListAdapter
import com.qure.memo.MemoListViewModel
import com.qure.memo.detail.DetailMemoActivity.Companion.MEMO_DATA
import com.qure.memo.model.MemoUI
import com.qure.memo.model.toTedClusterItem
import com.qure.navigator.DetailMemoNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ted.gun0912.clustering.clustering.TedClusterItem
import ted.gun0912.clustering.naver.TedNaverClustering
import javax.inject.Inject

@AndroidEntryPoint
class MapActivity : BaseActivity<ActivityMapBinding>(R.layout.activity_map), OnMapReadyCallback,
    OnMapClickListener {

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
        memoViewModel.getFilteredMemo()
        openMapFragment()
        initView()
        initRecyclerView()
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
        memoViewModel.getFilteredMemo()
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    memoViewModel.uiState.collect { uiState ->
                        if (uiState.isFilterInitialized) {
                            createMarkers(uiState.filteredMemo)
                        }
                    }
                }
            }
        }
    }

    private fun createMarkers(memos: List<MemoUI>) {
        val clusterItems = memos.map { it.toTedClusterItem() }
        TedNaverClustering.with<TedClusterItem>(this, naverMap)
            .customMarker { clusterItem ->
                Marker().apply {
                    icon =
                        OverlayImage.fromResource(com.qure.core_design.R.drawable.bg_map_fill_marker)
                    width = 100
                    height = 120
                }
            }
            .customCluster {
                TextView(this).apply {
                    setBackgroundResource(com.qure.core_design.R.drawable.bg_oval_gray600)
                    setTextColor(Color.WHITE)
                    setPadding(40, 25, 40, 25)

                    text = when {
                        it.size > 99 -> "99+"
                        else -> "${it.size}"
                    }
                }
            }
            .minClusterSize(1)
            .markerClickListener { memoUi ->
                val (lng, lat) = memoUi.getTedLatLng()
                val selectedMemos = memos.filter { it.coords == "${lat},${lng}" }

                setBottomSheetView(selectedMemos)
            }
            .clusterClickListener { clusterItems ->
                val clusterMemos = mutableSetOf<MemoUI>()
                clusterItems.items.forEach {
                    val latLng = it.getTedLatLng().toReverseCoordsString()
                    val filteredMemos = memos.filter { it.coords == latLng }
                    clusterMemos.addAll(filteredMemos)
                }
                setBottomSheetView(clusterMemos.toList())
            }
            .clusterBuckets(intArrayOf(10, 100, 1000, 10000))
            .items(clusterItems)
            .make()
    }

    private fun setBottomSheetView(memos: List<MemoUI>) {
        changeBottomSheetPeekHeight(300)
        binding.bottomSheetActivityMap.textViewBottomSheetMemoListCount.text =
            "${memos.size}개의 메모"
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
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.setOnMapClickListener(this)
        setMapSettings()
        updateCurrentLocation()
        observe()
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

    override fun onMapClick(p0: PointF, p1: LatLng) {
        changeBottomSheetPeekHeight(50)
    }

    private fun changeBottomSheetPeekHeight(height: Int) {
        val bottomSheet =
            BottomSheetBehavior.from(binding.bottomSheetActivityMap.constraintLayoutBottomSheetMemoList)
        bottomSheet.setPeekHeight(height.dpToPx(this), true)

        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheetView: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    bottomSheetView.animate()
                        .translationY(0f)
                }
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetView.animate()
                        .setDuration(200)
                        .translationY(20f)
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetView.animate()
                        .setDuration(200)
                        .translationY(-20f)
                }
            }

            override fun onSlide(bottomSheetView: View, slideOffset: Float) {
                when {
                    slideOffset < 0f -> {
                        if (bottomSheet.peekHeight == 300.dpToPx(this@MapActivity)) {
                            updateUiOnSlide(310, 250)
                        } else {
                            updateUiOnSlide(60, 0)
                        }
                    }

                    slideOffset > 0f -> {
                        if (bottomSheet.peekHeight == 300.dpToPx(this@MapActivity)) {
                            updateUiOnSlide(310, 250)
                        } else {
                            updateUiOnSlide(60, 0)
                        }
                    }
                }
            }

        })
    }

    private fun updateUiOnSlide(mapUIheight: Int, layoutHeight: Int) {
        val uiSettings = naverMap.uiSettings
        uiSettings.apply {
            isCompassEnabled = false
            isZoomControlEnabled = false
            setLogoMargin(0, 0, 0, mapUIheight.dpToPx(this@MapActivity))
        }
        val layoutParams =
            binding.constraintLayoutActivityMap.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(0, 0, 0, layoutHeight.dpToPx(this@MapActivity))
        binding.constraintLayoutActivityMap.layoutParams = layoutParams
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}