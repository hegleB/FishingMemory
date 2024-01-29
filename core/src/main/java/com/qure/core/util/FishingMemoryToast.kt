package com.qure.core.util

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.qure.core.R

class FishingMemoryToast {
    fun show(
        context: Context,
        title: String,
        duration: Int = Toast.LENGTH_SHORT,
    ) {
        val inflater = LayoutInflater.from(context)
        val toast = Toast(context)
        val view = inflater.inflate(R.layout.toast_fishing_memory, null)
        val textView: TextView = view.findViewById(R.id.textView_toast)
        textView.text = title
        toast.setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 0)
        toast.view = view
        toast.duration = duration
        toast.show()
    }

    fun error(
        context: Context,
        title: String?,
        duration: Int = Toast.LENGTH_SHORT,
    ) {
        if (title != null) {
            val inflater = LayoutInflater.from(context)
            val toast = Toast(context)
            val view = inflater.inflate(R.layout.error_toast_fishing_memory, null)
            val textView: TextView = view.findViewById(R.id.textView_toast)
            textView.text = title
            toast.setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 0)
            toast.view = view
            toast.duration = duration
            toast.show()
        }
    }
}
