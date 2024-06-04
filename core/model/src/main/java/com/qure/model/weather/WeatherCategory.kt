package com.qure.model.weather

import kotlinx.serialization.Serializable

@Serializable
enum class WeatherCategory {
    T1H,
    RN1,
    SKY,
    UUU,
    VVV,
    REH,
    PTY,
    LGT,
    VEC,
    WSD,
    UNKNOWN,
}
