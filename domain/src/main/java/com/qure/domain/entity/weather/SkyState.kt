package com.qure.domain.entity.weather

enum class SkyState(
    private val value: String,
) {
    SUNNY("맑음"),
    PARTLY_CLOUDY("구름조금"),
    CLOUDY("흐림"),
    ;

    companion object {
        fun from(value: Int) =
            when (value) {
                1 -> SUNNY.value
                3 -> PARTLY_CLOUDY.value
                else -> CLOUDY.value
            }
    }
}
