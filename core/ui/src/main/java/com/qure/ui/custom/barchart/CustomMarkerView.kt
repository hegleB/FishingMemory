package com.qure.ui.custom.barchart

import android.content.Context
import android.graphics.Canvas
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.qure.core.ui.R

class CustomMarkerView
    @JvmOverloads
    constructor(
        context: Context,
        defStyleRes: Int,
    ) : MarkerView(context, defStyleRes) {
        private var tvContent: TextView = findViewById(R.id.textView_markerView)

        override fun draw(canvas: Canvas) {
            canvas.translate(-(width / 2).toFloat(), -(height / 5).toFloat())

            super.draw(canvas)
        }


        override fun refreshContent(
            e: Entry?,
            highlight: Highlight?,
        ) {
            tvContent.text = e?.y?.toInt().toString()
            super.refreshContent(e, highlight)
        }
    }
