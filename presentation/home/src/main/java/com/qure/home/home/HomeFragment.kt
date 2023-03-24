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
import com.qure.core_design.custom.barchart.BarChartView
import com.qure.domain.entity.weather.SkyState
import com.qure.home.R
import com.qure.home.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var fusedLocationProvierClient: FusedLocationProviderClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observe()
    }

    private fun getCurrentLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {

                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
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
                                    val latXlngY = GpsTransfer().convertGRID_GPS(0, location.latitude, location.longitude)
                                    viewModel.fetchWeater(latXlngY)
                                    binding.textViewFragmentHomeLocation.text = getCurrentAddress(latXlngY)
                                    fusedLocationProvierClient.removeLocationUpdates(this)
                                }
                            }
                        }
                        fusedLocationProvierClient.requestLocationUpdates(request, locationCallback, null)
                    } else {
                        val latXlngY =
                            GpsTransfer().convertGRID_GPS(0, location.latitude, location.longitude)
                        viewModel.fetchWeater(latXlngY)
                        binding.textViewFragmentHomeLocation.text = getCurrentAddress(latXlngY)
                    }
                }

            } else {
                val intent = Intent(Settings.ACTION_SOUND_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    private fun getCurrentAddress(latXLngY: LatXLngY): String {
        val geoCoder = Geocoder(requireContext(), Locale.getDefault())
        val address =
            geoCoder.getFromLocation(latXLngY.lat, latXLngY.lng, 7)?.get(0)?.getAddressLine(0)
        val city = address?.split(" ")?.get(1).toString()
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
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Timber.d("")
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

    override fun onResume() {
        super.onResume()
    }

    private fun initView() {
        val values = listOf(1, 2, 3, 4, 15).map { it.toFloat() }
        val lables = listOf("붕어", "잉어", "밀어", "송어", "살치")

        BarChartView(
            requireContext(),
            resources,
            values,
            lables,
        ).initBarChart(binding.barChartFragmentHomeChart)
        fusedLocationProvierClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        getCurrentLocation()
    }

    private fun observe() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.UiState.collect {
                        setWeatherAnimation(it)
                    }
                }
            }
        }
    }

    private fun setWeatherAnimation(uiState: UiState) {
        if (uiState.isWeatherInitialized) {
            binding.lottieAnimationViewFragmentHomeWeather.setAnimation(
                uiState.getWeahterState()
            )
            binding.textViewFragmentHomeTemperature.text = uiState.toTemperatureString()
            binding.textViewFragmentHomeWeatherState.text =
                getSkyStateToString(uiState.toWeatherString()).value
        }
    }

    private fun getSkyStateToString(skyState: String): SkyState {
        return when (skyState.toInt()) {
            in (0..5) -> SkyState.SUNNY
            in (6..8) -> SkyState.PARTLY_CLOUDY
            else -> SkyState.CLOUDY
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }
}