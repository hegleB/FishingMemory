package com.qure.ui.model

import com.qure.model.FishingSpotUI


sealed interface FishingPlaceInfo {
    data class MemoInfo(val memoUI: MemoUI) : FishingPlaceInfo
    data class FishingSpotInfo(val fishingSpotUI: FishingSpotUI) : FishingPlaceInfo
}