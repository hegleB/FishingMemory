package com.qure.data.entity.map

import com.qure.model.map.Results
import com.qure.model.map.Status
import kotlinx.serialization.Serializable

@Serializable
data class ReverseGeocodingEntity(
    val status: Status,
    val results: List<Results>,
)
