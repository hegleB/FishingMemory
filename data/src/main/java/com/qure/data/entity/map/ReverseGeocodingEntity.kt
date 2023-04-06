package com.qure.data.entity.map

import com.qure.domain.entity.map.Results
import com.qure.domain.entity.map.Status


data class ReverseGeocodingEntity(
    val status: Status,
    val results: List<Results>,
)
