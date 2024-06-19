package com.qure.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    val response: Response,
) {
    companion object {
        val EMPTY = Weather(
            response = Response()
        )
    }
}

@Serializable
data class Response(
    val header: Header = Header(0, ""),
    val body: Body? = null,
)

@Serializable
data class Header(
    val resultCode: Int,
    val resultMsg: String,
)

@Serializable
data class Body(
    val dataType: String,
    val items: Items,
)

@Serializable
data class Items(
    val item: List<Item>,
)

@Serializable
data class Item(
    val baseDate: Int,
    val baseTime: Int,
    val category: WeatherCategory,
    val fcstDate: Int,
    val fcstTime: Int,
    val fcstValue: String,
    val nx: Int,
    val ny: Int,
)
