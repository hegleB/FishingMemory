package com.qure.home.home

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.*
import com.qure.core.BaseFragment
import com.qure.core.extensions.Spacing
import com.qure.core.util.FishingMemoryToast
import com.qure.core.util.setOnSingleClickListener
import com.qure.core_design.custom.barchart.BarChartView
import com.qure.domain.entity.memo.MemoFields
import com.qure.domain.entity.weather.SkyState
import com.qure.home.R
import com.qure.home.databinding.FragmentHomeBinding
import com.qure.home.home.memo.MemoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel by viewModels<HomeViewModel>()

    private val adapter: MemoAdapter by lazy { MemoAdapter() }
    private var memoFields: List<MemoFields> = listOf()
    private lateinit var fusedLocationProvierClient: FusedLocationProviderClient
    private var latX = 0.0
    private var longY = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        initView()
        initRecyclerView()
        refreshWeather()
    }

    private fun initRecyclerView() {
        binding.recyclerViewFragmentHomePost.adapter = adapter
    }

    private fun refreshWeather() {
        if (checkPermission()) {
            getCurrentLocation()
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

        fusedLocationProvierClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        getCurrentLocation()

        binding.chipGroupFragmentHome.setOnCheckedChangeListener { group, checkedId ->
            initBarChart(checkedId)
        }

        binding.imageViewFragmentHomeRefresh.setOnSingleClickListener {
            refreshWeather()
        }
    }

    private fun initBarChart(checkedId: Int) {
        val chartData = when (checkedId) {
            R.id.chip_fragmentHome_fishType -> memoFields.map { it.fishType.stringValue }
            R.id.chip_fragmentHome_fishSize -> memoFields.map { it.fishSize.stringValue }
            else -> memoFields.map { it.location.stringValue.split(String.Spacing)[1] }
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
                FishingMemoryToast().show(
                    requireContext(),
                    errorMessage,
                )
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
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
            uiState.isWeatherInitialized -> {
                setWeatherAnimation(uiState)
                val weatherData = uiState.weatherUI ?: emptyList()
                if (weatherData.isNotEmpty()) {
                    val latXLngY = LatXLngY(lat = latX, lng = longY)
                    binding.textViewFragmentHomeLocation.text =
                        getCurrentAddress(latXLngY)
                }
            }
            uiState.isFilterInitialized -> {
                adapter.submitList(uiState.filteredMemo)
                memoFields = uiState.filteredMemo.map { it.fields!!.fields }
                initBarChart(R.id.chip_fragmentHome_fishType)
            }
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