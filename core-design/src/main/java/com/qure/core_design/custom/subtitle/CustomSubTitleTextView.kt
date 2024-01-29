package com.qure.core_design.custom.subtitle

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.qure.core_design.R

class CustomSubTitleTextView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyle: Int = 0,
    ) : LinearLayout(context, attrs, defStyle) {
        lateinit var textView: TextView

        init {
            init(context)
            getAttrs(attrs)
        }

        private fun init(context: Context?) {
            val view =
                LayoutInflater.from(context).inflate(R.layout.custom_subtitle, this, false)
            addView(view)

            textView = findViewById(R.id.textView_subTitle)
        }

        private fun getAttrs(attrs: AttributeSet) {
            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomSubTitle)
            setTypeArray(typeArray)
        }

        private fun setTypeArray(typeArray: TypedArray) {
            val textId = typeArray.getString(R.styleable.CustomSubTitle_subTitleText)
            textView.text = textId
            typeArray.recycle()
        }
    }
