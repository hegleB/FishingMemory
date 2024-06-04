package com.qure.model.fishingspot

import com.qure.model.memo.FieldFilter
import kotlinx.serialization.Serializable

@Serializable
data class FishingSpotQuery(
    val structuredQuery: StructuredQuery,
)

@Serializable
data class StructuredQuery(
    val from: List<CollectionId>,
    val where: Where,
)

@Serializable
data class CollectionId(
    val collectionId: String,
)

@Serializable
data class Where(
    val fieldFilter: FieldFilter,
)
