package com.qure.home.home

import android.os.Bundle
import android.view.View
import com.qure.core.BaseFragment
import com.qure.home.R
import com.qure.home.databinding.FragmentHomeBinding
import com.qure.core_design.custom.barchart.BarChartView
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val valueSize = 5

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val values = listOf(1, 2, 3, 4, 5).map { it.toFloat() }
        val lables = listOf("붕어", "잉어", "밀어", "송어", "살치")

        BarChartView(
            requireContext(),
            resources,
            values,
            lables,
        ).initBarChart(binding.barChartFragmentHomeChart)
        binding.imageViewFragmentHomeRefresh.setOnClickListener {
            binding.lottieAnimationViewFragmentHomeWeather.setAnimation(getWeahterState(6, 0))
        }
        binding.lottieAnimationViewFragmentHomeWeather.setAnimation(getWeahterState(6, 0))
    }

    private fun getSkyState(sky: Int): Int {
        return when (sky) {
            in 0..5 -> if (getHour() in 6..17) R.raw.weather_sunny_day else R.raw.weather_sunny_night
            in 6..8 -> if (getHour() in 6..17) R.raw.weather_partly_cloudy_day else R.raw.weather_partly_cloudy_night
            else -> R.raw.weather_cloudey
        }
    }

    private fun getWeahterState(sky: Int, pty: Int): Int {
        return when (pty) {
            in listOf(
                1,
                2,
                5
            ) -> if (getHour() in 6..17) R.raw.weather_rainy_day else R.raw.weather_rainy_night
            in listOf(
                3,
                6,
                7
            ) -> if (getHour() in 6..17) R.raw.weather_snow_day else R.raw.weather_snow_night
            else -> getSkyState(sky)
        }
    }

    private fun getHour(): Int {
        val now = System.currentTimeMillis()
        val currentTime = Date(now)
        val formatTime = SimpleDateFormat("HH")
        return formatTime.format(currentTime).toInt()
    }
}