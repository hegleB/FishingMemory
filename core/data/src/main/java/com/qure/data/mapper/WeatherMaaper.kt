package com.qure.data.mapper

import com.qure.data.entity.weather.WeatherEntity
import com.qure.data.entity.weather.WeatherLocalEntity
import com.qure.model.weather.Weather

fun WeatherEntity.toWeather(): Weather {
    return Weather(
        response = this.response,
    )
}

fun WeatherLocalEntity.toWeather(): Weather {
    return Weather(
        response = this.response,
    )
}