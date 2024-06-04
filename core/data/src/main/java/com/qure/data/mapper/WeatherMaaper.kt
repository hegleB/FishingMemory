package com.qure.data.mapper

import com.qure.data.entity.weather.WeatherEntity
import com.qure.model.weather.Weather

fun WeatherEntity.toWeather(): Weather {
    return Weather(
        response = response,
    )
}
