package com.qure.domain.entity.map

data class Geocoding(
    val status: String,
    val meta: Meta?,
    val addresses: Array<Addresses>?,
    val errorMessage: String,
)

data class Meta(
    val totalCount: Int,
    val page: Int,
    val count: Int,
)

data class Addresses(
    val roadAddress: String,
    val jibunAddress: String,
    val englishAddress: String,
    val addressElements: Array<Region>,
    val x: String,
    val y: String,
    val distance: Double,
)

data class Region(
    val types: List<String>,
    val longName: String,
    val shortName: String,
    val code: String,
)
