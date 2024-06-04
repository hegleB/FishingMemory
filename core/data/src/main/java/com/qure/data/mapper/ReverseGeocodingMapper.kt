package com.qure.data.mapper

import com.qure.data.entity.map.ReverseGeocodingEntity
import com.qure.model.map.ReverseGeocoding

fun ReverseGeocodingEntity.toReverseGeocoding(): ReverseGeocoding {
    return ReverseGeocoding(
        status = this.status,
        results = this.results,
    )
}
