package com.qure.data.entity.geocoding

import com.qure.domain.entity.geocoding.Addresses
import com.qure.domain.entity.geocoding.Meta

data class GeocodingEntity(
    val status: String,
    val meta: Meta,
    val addresses: Array<Addresses>,
    val errorMessage: String,
)