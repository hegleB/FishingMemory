package com.qure.data.entity.weather

import com.qure.model.weather.Response
import kotlinx.serialization.Serializable

@Serializable
data class WeatherEntity(
    val response: Response,
)
