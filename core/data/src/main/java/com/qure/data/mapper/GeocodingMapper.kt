package com.qure.data.mapper

import com.qure.data.entity.map.GeocodingEntity
import com.qure.model.map.Geocoding

fun GeocodingEntity.toGeocoding(): Geocoding {
    return Geocoding(
        status = this.status,
        meta = this.meta,
        addresses = this.addresses,
        errorMessage = this.errorMessage,
    )
}
