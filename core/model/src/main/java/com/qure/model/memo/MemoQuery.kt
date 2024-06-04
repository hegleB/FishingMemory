package com.qure.model.memo

import kotlinx.serialization.Serializable

@Serializable
data class MemoQuery(
    val structuredQuery: StructuredQuery,
)

@Serializable
data class StructuredQuery(
    val from: List<CollectionId>,
    val orderBy: List<OrderBy>,
    val where: Where,
)

@Serializable
data class CollectionId(
    val collectionId: String,
)

@Serializable
data class Where(
    val compositeFilter: CompositeFilter,
)

@Serializable
data class OrderBy(
    val field: FieldPath,
    val direction: String,
)

@Serializable
data class CompositeFilter(
    val op: String,
    val filters: List<Filter>,
)

@Serializable
data class Filter(
    val fieldFilter: FieldFilter,
)

@Serializable
data class FieldFilter(
    val field: FieldPath,
    val op: String,
    val value: Value,
)

@Serializable
data class FieldPath(
    val fieldPath: String,
)

@Serializable
data class Value(
    val stringValue: String,
)
