package com.qure.core_design.custom.barchart

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
import com.github.mikephil.charting.formatter.ValueFormatter
import com.qure.core_design.R

class BarChartView(
    private val context: Context,
    private val resources: Resources,
    private val values: List<Float>,
    private val labels: List<String>,
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

        val xAxisFormatter: ValueFormatter = EllipsizeXAxisFormatter(labels)
        val xAxis = barChart.xAxis

        xAxis.apply {
            valueFormatter = xAxisFormatter
            labelCount = labels.size
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            granularity = 1f // only intervals of 1 day
            yOffset = 15f
            textSize = 12f
            textColor = ContextCompat.getColor(context, R.color.gray_700)
            axisLineColor = ContextCompat.getColor(context, R.color.gray_700)
        }

        val leftAxis = barChart.axisLeft
        leftAxis.apply {
            setLabelCount(3, true)
            setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
            setDrawGridLines(false)
            spaceTop = 0f
            axisMinimum = 0f // this replaces setStartAtZero(true)
            if (values.size > 0) {
                axisMaximum = values.max()
            }
            textColor = ContextCompat.getColor(context, R.color.gray_700)
            axisLineColor = ContextCompat.getColor(context, R.color.gray_700)
            zeroLineColor = ContextCompat.getColor(context, R.color.gray_700)
            textSize = 12f
            xOffset = 15f
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
            xEntrySpace = 0f
        }

        setData(barChart)
    }

    private fun setData(barChart: BarChart) {
        // Zoom In / Out 가능 여부 설정
        barChart.setScaleEnabled(false)

        val valueList = ArrayList<BarEntry>()

        if (values.isNotEmpty()) {
            for (i in 0 until values.size) {
                valueList.add(BarEntry(i.toFloat(), values[i]))
            }
        }

        val barDataSet = BarDataSet(valueList, null)
        barDataSet.setColors(
            resources.getColor(R.color.blue_500),
        )
        barDataSet.setDrawValues(false)
        barDataSet.isHighlightEnabled = true
        barDataSet.setDrawIcons(true)
        barDataSet.highLightColor = resources.getColor(R.color.blue_100)

        val data = BarData(barDataSet)
        data.barWidth = 0.3f

        barChart.apply {
            setDrawGridBackground(false)
            animateXY(1000, 1000)
            this.data = data
            isDragXEnabled = true
            marker = CustomMarkerView(context, R.layout.custom_marker_view)
            setVisibleXRange(5f, 5f)
            moveViewToX(-1f)
            setFitBars(true)
            invalidate()
        }
    }
}

class EllipsizeXAxisFormatter(private val labels: List<String>) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val index = value.toInt()
        return if (index >= 0 && index < labels.size) {
            val label = labels[index]
            if (label.length > 4) {
                label.substring(0, 4) + "..."
            } else {
                label
            }
        } else {
            value.toString()
        }
    }
}
