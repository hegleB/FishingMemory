package com.qure.data.entity.fishingspot

import com.qure.model.fishingspot.Document
import kotlinx.serialization.Serializable

@Serializable
data class FishingSpotEntity(
    val document: Document,
    val readTime: String,
)
