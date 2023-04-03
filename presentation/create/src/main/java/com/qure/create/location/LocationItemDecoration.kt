package com.qure.create.location

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class LocationItemDecoration(private val divWidth: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = divWidth
        outRect.right = divWidth
        outRect.top = divWidth
        outRect.bottom = divWidth
    }
}