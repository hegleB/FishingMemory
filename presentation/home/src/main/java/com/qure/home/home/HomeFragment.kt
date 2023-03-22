package com.qure.home.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qure.core.BaseFragment
import com.qure.home.R
import com.qure.home.databinding.FragmentHomeBinding
import com.qure.core_design.custom.barchart.BarChartView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val valueSize = 5

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val values = listOf(1, 2, 3, 4, 5).map { it.toFloat() }
        val lables = listOf("붕어", "잉어", "밀어", "송어", "살치")

        BarChartView(
            requireContext(),
            resources,
            values,
            lables,
        ).initBarChart(binding.barChartFragmentHomeChart)
    }
}