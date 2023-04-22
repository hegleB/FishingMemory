package com.qure.home.home

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.children
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.*
import com.qure.core.BaseFragment
import com.qure.core.extensions.Spacing
import com.qure.core.extensions.gone
import com.qure.core.extensions.initSwipeRefreshLayout
import com.qure.core.extensions.visiable
import com.qure.core.util.FishingMemoryToast
import com.qure.core.util.setOnSingleClickListener
import com.qure.core_design.custom.barchart.BarChartView
import com.qure.domain.entity.weather.SkyState
import com.qure.home.R
import com.qure.home.databinding.FragmentHomeBinding
import com.qure.home.home.memo.MemoAdapter
import com.qure.memo.detail.DetailMemoActivity
import com.qure.memo.detail.DetailMemoActivity.Companion.MEMO_DATA
import com.qure.memo.model.MemoUI
import com.qure.navigator.DetailMemoNavigator
import com.qure.navigator.MemoNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    @Inject
    lateinit var memoNavigator: MemoNavigator

    @Inject
    lateinit var detailMemoNavigator: DetailMemoNavigator

    private val viewModel by viewModels<HomeViewModel>()
    private var memos: List<MemoUI> = emptyList()

    private val adapter: MemoAdapter by lazy {
        MemoAdapter(
            onMemoClick = { memo ->
                val intent = detailMemoNavigator.intent(requireContext())
                intent.putExtra(MEMO_DATA, memo)
                startActivity(intent)

            }
        )
    }
    private lateinit var fusedLocationProvierClient: FusedLocationProviderClient
    private var latX = 0.0
    private var longY = 0.0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fusedLocationProvierClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        getCurrentLocation()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        initView()
        initRecyclerView()

    }

    private fun initRecyclerView() {
        binding.recyclerViewFragmentHomePost.adapter = adapter
    }

    private fun refreshWeather() {
        if (checkPermission()) {
            binding.progressBarFragmentHome.visiable()
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    delay(500)
                    launch {
                        viewModel.UiState.collect {
                            if (it.isWeatherInitialized) {
                                handleWeatherInitialized(it)
                                binding.progressBarFragmentHome.gone()
                            }
                        }
                    }
                }
            }
        } else {
            requestPermission()
        }
    }

    private fun getCurrentLocation() {
        if (!checkPermission()) {
            requestPermission()
            return
        }
        if (!isLocationEnabled()) {
            val intent = Intent(Settings.ACTION_SOUND_SETTINGS)
            startActivity(intent)
            return
        }
        fusedLocationProvierClient.lastLocation.addOnCompleteListener { task ->
            val location = task.result
            if (location == null) {
                val request = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(300)
                    .setFastestInterval(200)
                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        for (location in locationResult.locations) {
                            val latXlngY = GpsTransfer().convertGRID_GPS(
                                0,
                                location.latitude,
                                location.longitude
                            )
                            viewModel.fetchWeater(latXlngY)
                            fusedLocationProvierClient.removeLocationUpdates(this)
                        }
                    }
                }
                fusedLocationProvierClient.requestLocationUpdates(request, locationCallback, null)
            } else {
                latX = location.latitude
                longY = location.longitude
                val latXlngY = GpsTransfer().convertGRID_GPS(0, latX, longY)
                viewModel.fetchWeater(latXlngY)

            }
        }
    }

    private fun getCurrentAddress(latXLngY: LatXLngY): String {
        val geoCoder = Geocoder(requireContext(), Locale.getDefault())
        val address =
            geoCoder.getFromLocation(latXLngY.lat, latXLngY.lng, 7)?.get(0)?.getAddressLine(0)
        val city = address?.split(String.Spacing)?.get(1).toString()
        return city
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                FishingMemoryToast().show(requireContext(), "위치 권한을 설정해주세요")
            }
        }
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun initView() {
        viewModel.getFilteredMemo()

        binding.swipeRefreshLayoutFragmentHome.initSwipeRefreshLayout()
        binding.swipeRefreshLayoutFragmentHome.setOnRefreshListener {
            getCurrentLocation()
            refreshView()
        }
        binding.swipeRefreshLayoutFragmentHome.setRefreshing(false)

        binding.chipGroupFragmentHome.setOnCheckedChangeListener { group, checkedId ->
            initBarChart(checkedId)
        }

        binding.imageViewFragmentHomeRefresh.setOnSingleClickListener {
            getCurrentLocation()
            refreshWeather()
        }

        binding.textViewFragmentHomeMemoMore.setOnSingleClickListener {
            startActivity(memoNavigator.intent(requireContext()))
        }
    }

    private fun refreshView() = lifecycleScope.launch {
        viewModel.getFilteredMemo()
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
            launch {
                delay(500)
                viewModel.UiState.collect {
                    binding.swipeRefreshLayoutFragmentHome.setRefreshing(false)
                    if (it.isFilterInitialized) {
                        handleFilterInitialized(it)
                    }

                    if (it.isWeatherInitialized) {
                        handleWeatherInitialized(it)
                    }
                }
            }
        }
    }


    private fun initBarChart(checkedId: Int) {
        val chartData = when (checkedId) {
            R.id.chip_fragmentHome_fishType -> memos.map { it.fishType }
            R.id.chip_fragmentHome_fishSize -> memos.map { it.fishSize }
            else -> memos.map {
                try {
                    it.location.split(String.Spacing)[1]
                } catch (e: IndexOutOfBoundsException) {
                    it.location.split(String.Spacing)[0]
                }
            }
        }
        BarChartView(
            requireContext(),
            resources,
            countElements(chartData).values.toList(),
            chartData.distinct(),
        ).initBarChart(binding.barChartFragmentHomeChart)
    }

    private fun observe() {
        viewModel.error
            .onEach { errorMessage ->
                FishingMemoryToast().error(
                    requireContext(),
                    errorMessage,
                )
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.UiState.collect {
                        handleUiState(it)
                    }
                }
            }
        }
    }

    private fun handleUiState(uiState: UiState) {
        when {
            uiState.isWeatherInitialized -> handleWeatherInitialized(uiState)
            uiState.isFilterInitialized -> handleFilterInitialized(uiState)
        }
    }

    private fun handleWeatherInitialized(uiState: UiState) {
        setWeatherAnimation(uiState)
        val weatherData = uiState.weatherUI ?: emptyList()
        if (weatherData.isNotEmpty()) {
            val latXLngY = LatXLngY(lat = latX, lng = longY)
            binding.textViewFragmentHomeLocation.text = getCurrentAddress(latXLngY)
            binding.progressBarFragmentHome.gone()
        }
    }

    private fun handleFilterInitialized(uiState: UiState) {
        if (uiState.filteredMemo.isEmpty()) {
            hideViews()
        } else {
            showViews()
            adapter.submitList(uiState.filteredMemo) {
                binding.recyclerViewFragmentHomePost.scrollToPosition(0)
            }
            memos = uiState.filteredMemo
            initBarChart(R.id.chip_fragmentHome_fishType)
        }
    }

    private fun hideViews() {
        with(binding) {
            barChartFragmentHomeChart.gone()
            chipGroupFragmentHome.gone()
            recyclerViewFragmentHomePost.gone()
            textViewFragmentHomeEmptyBarchart.visiable()
            textViewFragmentHomeEmptyRecyclerview.visiable()

        }
    }

    private fun showViews() {
        with(binding) {
            barChartFragmentHomeChart.visiable()
            chipGroupFragmentHome.visiable()
            recyclerViewFragmentHomePost.visiable()
            textViewFragmentHomeEmptyBarchart.gone()
            textViewFragmentHomeEmptyRecyclerview.gone()
        }
    }

    private fun <T> countElements(list: List<T>): Map<T, Float> {
        val map = mutableMapOf<T, Float>()
        for (element in list) {
            map[element] = (map.getOrDefault(element, 0)).toFloat() + 1
        }
        return map
    }

    private fun setWeatherAnimation(uiState: UiState) {
        binding.lottieAnimationViewFragmentHomeWeather.setAnimation(
            uiState.toWeatherAnimation()
        )
        binding.textViewFragmentHomeTemperature.text = uiState.toTemperatureString()
        binding.textViewFragmentHomeWeatherState.text =
            SkyState.from(uiState.getSkyState().fcstValue.toInt())
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }
}