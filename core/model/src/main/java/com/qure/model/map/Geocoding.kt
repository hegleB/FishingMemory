package com.qure.model.map

import kotlinx.serialization.Serializable

data class Geocoding(
    val status: String,
    val meta: Meta?,
    val addresses: Array<Addresses>?,
    val errorMessage: String,
)
@Serializable
data class Meta(
    val totalCount: Int,
    val page: Int,
    val count: Int,
)

@Serializable
data class Addresses(
    val roadAddress: String,
    val jibunAddress: String,
    val englishAddress: String,
    val addressElements: Array<Region>,
    val x: String,
    val y: String,
    val distance: Double,
)

@Serializable
data class Region(
    val types: List<String>,
    val longName: String,
    val shortName: String,
    val code: String,
)
