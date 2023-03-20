package com.qure.home.home.barchart

import android.content.Context
import android.content.res.Resources
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.qure.home.R

class BarChartView(
    private val context: Context,
    private val resources: Resources,
    private val values: List<Float>
) {

    fun initBarChart(barChart: BarChart) {
        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(false)
        barChart.setDrawGridBackground(true)

        barChart.description.isEnabled = false

        // if more than 60 entries are displayed in the chart, no values will be drawn
        barChart.setMaxVisibleValueCount(10)

        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false)
        barChart.isDoubleTapToZoomEnabled = false
        barChart.setScaleEnabled(false)

        barChart.setDrawGridBackground(false)

        // barChart.setDrawYLabels(false);
        val labels = arrayListOf("붕어", "잉어", "밀어", "송어", "살치")

        val xAxisFormatter: ValueFormatter = IndexAxisValueFormatter(labels)
        val xAxis = barChart.xAxis


        xAxis.apply {
            valueFormatter = xAxisFormatter
            labelCount = labels.size
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            granularity = 1f // only intervals of 1 day
            yOffset = 15f
            textColor = ContextCompat.getColor(context, com.qure.core_design.R.color.gray_300)
            axisLineColor =
                ContextCompat.getColor(context, com.qure.core_design.R.color.blue_600)
        }

        val leftAxis = barChart.axisLeft
        leftAxis.apply {
            setLabelCount(4)
            setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
            spaceTop = 0f
            axisMinimum = 0f // this replaces setStartAtZero(true)
            textColor = ContextCompat.getColor(context, com.qure.core_design.R.color.gray_300)
            axisLineColor = ContextCompat.getColor(context, com.qure.core_design.R.color.gray_300)
            zeroLineColor = ContextCompat.getColor(context, com.qure.core_design.R.color.gray_300)
        }

        barChart.axisRight.isEnabled = false

        val legend = barChart.legend

        legend.apply {
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            orientation = Legend.LegendOrientation.HORIZONTAL
            setDrawInside(false)
            form = Legend.LegendForm.NONE
            formSize = 9f
            textSize = 11f
            xEntrySpace = 1f
        }

        setData(barChart)
    }

    private fun setData(barChart: BarChart) {

        // Zoom In / Out 가능 여부 설정
        barChart.setScaleEnabled(false)

        val valueList = ArrayList<BarEntry>()

        for (i in 0 until  values.size) {
            valueList.add(BarEntry(i.toFloat(), values[i]))
        }

        val barDataSet = BarDataSet(valueList, null)
        barDataSet.setColors(
            resources.getColor(com.qure.core_design.R.color.blue_500),
        )
        barDataSet.setDrawValues(false)
        barDataSet.isHighlightEnabled = true
        barDataSet.setDrawIcons(true)
        barDataSet.highLightColor = resources.getColor(com.qure.core_design.R.color.blue_100)

        val data = BarData(barDataSet)
        data.barWidth = 0.3f

        barChart.apply {
            setDrawGridBackground(false)
            animateXY(1000, 1000)
            this.data = data
            isDragXEnabled = true
            marker = CustomMarkerView(context, R.layout.marker_view)
            setVisibleXRange(5f, 5f)
            moveViewToX(1f)
            setFitBars(true)
            invalidate()
        }

    }

}