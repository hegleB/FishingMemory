package com.qure.home.home

import android.os.Bundle
import android.view.View
import com.qure.core.BaseFragment
import com.qure.home.R
import com.qure.home.databinding.FragmentHomeBinding
import com.qure.home.home.barchart.BarChartView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val valueSize = 5

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val values = listOf(1, 2, 3, 4, 5).map { it.toFloat() }

        BarChartView(
            requireContext(),
            resources,
            values
        ).initBarChart(binding.barChartActivityHomeChart)
    }
}