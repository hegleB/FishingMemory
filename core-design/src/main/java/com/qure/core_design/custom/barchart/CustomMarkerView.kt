package com.qure.core_design.custom.barchart

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.qure.core_design.R

class CustomMarkerView @JvmOverloads constructor(
    context: Context,
    defStyleRes: Int,
) : MarkerView(context, defStyleRes) {

    private var tvContent: TextView

    init {
        tvContent = findViewById(R.id.textView_markerView)
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