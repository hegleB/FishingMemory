package com.qure.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PointF
import android.location.Location
import android.net.Uri
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
import com.qure.core.extensions.*
import com.qure.core.util.setOnSingleClickListener
import com.qure.domain.MEMO_DATA
import com.qure.domain.SPOT_DATA
import com.qure.domain.entity.MarkerType
import com.qure.map.databinding.ActivityMapBinding
import com.qure.memo.model.MemoUI
import com.qure.memo.model.toTedClusterItem
import com.qure.model.FishingSpotUI
import com.qure.model.toTedClusterItem
import com.qure.navigator.DetailMemoNavigator
import com.qure.navigator.FishingSpotNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ted.gun0912.clustering.BaseBuilder
import ted.gun0912.clustering.clustering.Cluster
import ted.gun0912.clustering.clustering.TedClusterItem
import ted.gun0912.clustering.naver.TedNaverClustering
import ted.gun0912.clustering.naver.TedNaverMarker
import javax.inject.Inject

@AndroidEntryPoint
class MapActivity :
    BaseActivity<ActivityMapBinding>(R.layout.activity_map),
    OnMapReadyCallback,
    OnMapClickListener {
    @Inject
    lateinit var detailMemoNavigator: DetailMemoNavigator

    @Inject
    lateinit var fishingSpotNavigator: FishingSpotNavigator

    private val viewModel by viewModels<MapViewModel>()

                    }
                    }
                }
            )
        }
    }
}
