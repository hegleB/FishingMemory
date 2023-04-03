package com.qure.create.location

import android.content.Context
import com.qure.create.R

class Region {
    companion object {
        fun getArray(context: Context, position: Int = -1): Array<String> {
            return when (position) {
                0 -> context.resources.getStringArray(R.array.array_region_seoul)
                1 -> context.resources.getStringArray(R.array.array_region_busan)
                2 -> context.resources.getStringArray(R.array.array_region_daegu)
                3 -> context.resources.getStringArray(R.array.array_region_incheon)
                4 -> context.resources.getStringArray(R.array.array_region_gwangju)
                5 -> context.resources.getStringArray(R.array.array_region_daejeon)
                6 -> context.resources.getStringArray(R.array.array_region_ulsan)
                7 -> context.resources.getStringArray(R.array.array_region_sejong)
                8 -> context.resources.getStringArray(R.array.array_region_gyeonggi)
                9 -> context.resources.getStringArray(R.array.array_region_gangwon)
                10 -> context.resources.getStringArray(R.array.array_region_chung_buk)
                11 -> context.resources.getStringArray(R.array.array_region_chung_nam)
                12 -> context.resources.getStringArray(R.array.array_region_gyeong_buk)
                13 -> context.resources.getStringArray(R.array.array_region_gyeong_nam)
                14 -> context.resources.getStringArray(R.array.array_region_jeon_buk)
                15 -> context.resources.getStringArray(R.array.array_region_jeon_nam)
                16 -> context.resources.getStringArray(R.array.array_region_jeju)
                else -> context.resources.getStringArray(R.array.array_region)
            }
        }
    }
}