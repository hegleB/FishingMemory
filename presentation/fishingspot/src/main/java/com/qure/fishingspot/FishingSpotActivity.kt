package com.qure.fishingspot

import android.content.Intent
import android.graphics.Outline
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import android.widget.TextView
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.qure.core.BaseActivity
import com.qure.core.util.setOnSingleClickListener
import com.qure.fishingspot.databinding.ActivityFishingSpotBinding
import com.qure.model.FishingSpotUI
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FishingSpotActivity :
    BaseActivity<ActivityFishingSpotBinding>(R.layout.activity_fishing_spot), OnMapReadyCallback {

    private var fishingSpot: FishingSpotUI = FishingSpotUI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        openMapFragment()
        setFishingSoptView()
        initView()
    }

    private fun initView() {
        binding.imageViewActivityFishingSpotBack.setOnSingleClickListener {
            finish()
        }

        binding.textViewActivityFishingSpotPhoneNumberData.setOnSingleClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:${fishingSpot.phone_number}"))
            startActivity(intent)
        }
    }

    private fun initData() {
        fishingSpot = intent.getParcelableExtra(SPOT_DATA) ?: FishingSpotUI()
    }

    private fun openMapFragment() {
        val fm = supportFragmentManager
        val frameLayout = findViewById<FrameLayout>(R.id.frameLayout_activityFishingSpot_map)
        frameLayout.clipToOutline = true
        val outline = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, 50f)
            }
        }
        frameLayout.outlineProvider = outline

        val mapFragment =
            fm.findFragmentById(R.id.frameLayout_activityFishingSpot_map) as MapFragment?
                ?: MapFragment.newInstance().also {
                    fm.beginTransaction()
                        .add(R.id.frameLayout_activityFishingSpot_map, it)
                        .commit()
                }
        mapFragment.getMapAsync(this)
    }

    private fun setFishingSoptView() {
        with(binding) {
            textViewActivityFishingSpotTitle.text = fishingSpot.fishing_spot_name
            textViewActivityFishingSpotName.text = fishingSpot.fishing_spot_name
            textViewActivityFishingSpotFeeData.text = fishingSpot.fee
            textViewActivityFishingSpotFishTypeData.text = fishingSpot.fish_type
            textViewActivityFishingSpotMainPointData.text = fishingSpot.main_point
            textViewActivityFishingSpotNumberAddressData.text = fishingSpot.number_address
            textViewActivityFishingSpotPhoneNumberData.text = fishingSpot.phone_number
            textViewActivityFishingSpotRoadAddressData.text = fishingSpot.road_address
            textViewActivityFishingSpotGroundType.text = fishingSpot.fishing_ground_type
            textViewActivityFishingSpotPhoneNumberData.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        }
    }


    override fun onMapReady(naverMap: NaverMap) {
        val latLng = LatLng(fishingSpot.latitude, fishingSpot.longitude)
        val cameraUpdate = CameraUpdate.scrollTo(latLng)
        val marker = Marker().apply {
            position = latLng
            height = 100
            width = 120
            icon = OverlayImage.fromResource(com.qure.core_design.R.drawable.bg_map_fill_marker)
        }
        marker.map = naverMap
        naverMap.moveCamera(cameraUpdate)
    }

    companion object {
        const val SPOT_DATA = "fishingSpotData"
    }
}