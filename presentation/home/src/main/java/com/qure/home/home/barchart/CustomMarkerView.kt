package com.qure.home.home.barchart

import android.content.Context
import android.graphics.Canvas
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.qure.home.R

class CustomMarkerView : MarkerView {

    private var tvContent: TextView

    // marker
    constructor(context: Context?, layoutResource: Int) : super(context, layoutResource) {

        tvContent = findViewById(R.id.test_marker_view)
    }

    // draw override를 사용해 marker의 위치 조정 (bar의 상단 중앙)
    override fun draw(canvas: Canvas?) {
        canvas!!.translate(-(width / 2).toFloat(), -(height / 5).toFloat())

        super.draw(canvas)
    }

    // entry를 content의 텍스트에 지정
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        tvContent.text = e?.y?.toInt().toString()
        super.refreshContent(e, highlight)
    }

}