package com.qure.data.mapper

import com.qure.data.entity.fishingspot.FishingSpotEntity
import com.qure.data.entity.fishingspot.FishingSpotLocalEntity
import com.qure.model.fishingspot.FishingSpot

fun FishingSpotEntity.toFishingSpot(): FishingSpot {
    return FishingSpot(
        document = this.document,
    )
}

fun FishingSpotLocalEntity.toFishingSpot(): FishingSpot {
    return FishingSpot(
        document = this.document,
    )
}

fun FishingSpot.toFishingSpotLocalEntity(): FishingSpotLocalEntity {
    return FishingSpotLocalEntity(
        document = this.document,
    )
}