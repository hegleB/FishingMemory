package com.qure.fishingspot

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Outline
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.qure.core.BaseActivity
import com.qure.core.util.FishingMemoryToast
import com.qure.core.util.setOnSingleClickListener
import com.qure.domain.SPOT_DATA
import com.qure.fishingspot.databinding.ActivityFishingSpotBinding
import com.qure.model.FishingSpotUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FishingSpotActivity :
    BaseActivity<ActivityFishingSpotBinding>(R.layout.activity_fishing_spot), OnMapReadyCallback {

    private val viewModel by viewModels<FishingSpotViewModel>()
    private var fishingSpot: FishingSpotUI = FishingSpotUI()
    private var animator: ValueAnimator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        openMapFragment()
        setFishingSoptView()
        initView()
        observe()
    }

    private fun initView() {
        viewModel.checkBookmark(fishingSpot.number)

        binding.imageViewActivityFishingSpotBack.setOnSingleClickListener {
            finish()
        }

        binding.textViewActivityFishingSpotPhoneNumberData.setOnSingleClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:${fishingSpot.phone_number}"))
            startActivity(intent)
        }

        val favoriteFishingSpot = binding.lottieAnimationActivityFishingSpotBookmark

        favoriteFishingSpot.setOnSingleClickListener {
            viewModel.toggleBookmarkButton(fishingSpot)
            animator?.start()
        }
    }

    private fun onBookmarkClicked(isBookmark: Boolean) {
        animator = if (!isBookmark) {
            ValueAnimator.ofFloat(0f, 0.6f).setDuration(1000)
        } else {
            ValueAnimator.ofFloat(0.4f, 0f).setDuration(700)
        }

        animator?.addUpdateListener { animation ->
            binding.lottieAnimationActivityFishingSpotBookmark.progress =
                animation.animatedValue as Float
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

    private fun observe() {
        viewModel.error
            .onEach { errorMessage -> FishingMemoryToast().error(this, errorMessage) }
            .launchIn(lifecycleScope)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect {
                        onBookmarkClicked(it.isBookmarkClicked)
                        if (it.isBookmarked) {
                            binding.lottieAnimationActivityFishingSpotBookmark.progress = 0.6f
                        } else {
                            binding.lottieAnimationActivityFishingSpotBookmark.progress = 0f
                        }
                    }
                }
            }
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
}