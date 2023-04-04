package com.qure.create.location

import android.content.Context
import com.qure.core.extensions.getStringArrayCompat
import com.qure.create.R

class Region {
    companion object {
        fun getArray(context: Context, position: Int = -1): Array<String> {
            return when (position) {
                0 -> context.getStringArrayCompat(R.array.array_region_seoul)
                1 -> context.getStringArrayCompat(R.array.array_region_busan)
                2 -> context.getStringArrayCompat(R.array.array_region_daegu)
                3 -> context.getStringArrayCompat(R.array.array_region_incheon)
                4 -> context.getStringArrayCompat(R.array.array_region_gwangju)
                5 -> context.getStringArrayCompat(R.array.array_region_daejeon)
                6 -> context.getStringArrayCompat(R.array.array_region_ulsan)
                7 -> context.getStringArrayCompat(R.array.array_region_sejong)
                8 -> context.getStringArrayCompat(R.array.array_region_gyeonggi)
                9 -> context.getStringArrayCompat(R.array.array_region_gangwon)
                10 -> context.getStringArrayCompat(R.array.array_region_chung_buk)
                11 -> context.getStringArrayCompat(R.array.array_region_chung_nam)
                12 -> context.getStringArrayCompat(R.array.array_region_gyeong_buk)
                13 -> context.getStringArrayCompat(R.array.array_region_gyeong_nam)
                14 -> context.getStringArrayCompat(R.array.array_region_jeon_buk)
                15 -> context.getStringArrayCompat(R.array.array_region_jeon_nam)
                16 -> context.getStringArrayCompat(R.array.array_region_jeju)
                else -> context.getStringArrayCompat(R.array.array_region)
            }
        }
    }
}