package com.qure.data.entity.map

import com.qure.domain.entity.map.Addresses
import com.qure.domain.entity.map.Meta

data class GeocodingEntity(
    val status: String,
    val meta: Meta,
    val addresses: Array<Addresses>,
    val errorMessage: String,
)