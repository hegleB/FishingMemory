package com.qure.domain.entity.memo

data class MemoQuery(
    val structuredQuery: StructuredQuery
)

data class StructuredQuery(
    val from: List<CollectionId>,
    val orderBy: List<OrderBy>,
    val where: Where,
)

data class CollectionId(
    val collectionId: String
)

data class Where(
    val compositeFilter: CompositeFilter
)

data class OrderBy(
    val field: FieldPath,
    val direction: String,
)

data class CompositeFilter(
    val op: String,
    val filters: List<Filter>
)

data class Filter(
    val fieldFilter: FieldFilter
)

data class FieldFilter(
    val field: FieldPath,
    val op: String,
    val value: Value
)

data class FieldPath(
    val fieldPath: String
)

data class Value(
    val stringValue: String
)