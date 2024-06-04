package com.qure.data.mapper

import com.qure.data.entity.fishingspot.FishingSpotEntity
import com.qure.model.fishingspot.FishingSpot

fun FishingSpotEntity.toFishingSpot(): FishingSpot {
    return FishingSpot(
        document = this.document,
        readTime = this.readTime,
    )
}
