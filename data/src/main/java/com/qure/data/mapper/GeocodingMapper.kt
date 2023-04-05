package com.qure.data.mapper


import com.qure.data.entity.geocoding.GeocodingEntity
import com.qure.domain.entity.geocoding.Geocoding


fun GeocodingEntity.toGeocoding(): Geocoding {
    return Geocoding(
        status = this.status,
        meta = this.meta,
        addresses = this.addresses,
        errorMessage = this.errorMessage,
    )
}