package com.qure.core_design.custom.barchart

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.google.android.material.chip.Chip
import com.qure.core_design.R

class CustomChartChipView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyle: Int = 0,
        defStyleRes: Int = 0,
    ) : LinearLayout(context, attrs) {
        lateinit var chip: Chip

        init {
            init(context)
            getAttrs(attrs)
            getAttrs(attrs, defStyle, defStyleRes)
        }

        private fun init(context: Context) {
            val view = LayoutInflater.from(context).inflate(R.layout.custom_chartchip, this, false)
            addView(view)
            chip = findViewById(R.id.chip_chartType)
        }

        private fun getAttrs(attrs: AttributeSet) {
            val typeArray =
                context.obtainStyledAttributes(
                    attrs,
                    R.styleable.CustomChartChip,
                )

            setTypeArray(typeArray)
        }

        private fun getAttrs(
            attrs: AttributeSet,
            defStyle: Int,
            defStyleRes: Int,
        ) {
            val typeArray =
                context.obtainStyledAttributes(
                    attrs,
                    R.styleable.CustomChartChip,
                    defStyle,
                    defStyleRes,
                )

            setTypeArray(typeArray)
        }

        private fun setTypeArray(typeArray: TypedArray) {
            chip.text = typeArray.getString(R.styleable.CustomChartChip_chartChipText)
            typeArray.recycle()
        }
    }
