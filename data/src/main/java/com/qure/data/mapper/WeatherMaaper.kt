package com.qure.data.mapper

import com.qure.data.entity.weather.WeatherEntity
import com.qure.domain.entity.weather.Weather

fun WeatherEntity.toWeather(): Weather {
    return Weather(
        response = response
    )
}