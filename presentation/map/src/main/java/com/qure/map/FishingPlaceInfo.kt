package com.qure.map

import com.qure.memo.model.MemoUI
import com.qure.model.FishingSpotUI

sealed interface FishingPlaceInfo {
    data class MemoInfo(val memoUI: MemoUI) : FishingPlaceInfo
    data class FishingSpotInfo(val fishingSpotUI: FishingSpotUI) : FishingPlaceInfo
}