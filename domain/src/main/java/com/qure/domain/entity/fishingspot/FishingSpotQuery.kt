package com.qure.domain.entity.fishingspot

import com.qure.domain.entity.memo.CollectionId
import com.qure.domain.entity.memo.FieldFilter

data class FishingSpotQuery(
    val structuredQuery: StructuredQuery
)

data class StructuredQuery(
    val from: List<CollectionId>,
    val where: Where,
)

data class CollectionId(
    val collectionId: String
)

data class Where(
    val fieldFilter: FieldFilter
)