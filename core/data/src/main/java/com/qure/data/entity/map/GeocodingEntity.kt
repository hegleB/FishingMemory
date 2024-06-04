package com.qure.data.entity.map

import com.qure.model.map.Addresses
import com.qure.model.map.Meta
import kotlinx.serialization.Serializable

@Serializable
data class GeocodingEntity(
    val status: String,
    val meta: Meta,
    val addresses: Array<Addresses>,
    val errorMessage: String,
)
