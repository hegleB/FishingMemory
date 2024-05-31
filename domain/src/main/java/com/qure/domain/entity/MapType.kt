package com.qure.domain.entity

enum class MapType(val value: String) {
    BASIC_MAP("일반지도"),
    SATELLITE_MAP("위성지도"),
    TERRAIN_MAP("지형지도");

    companion object {
        fun getMapType(type: String): MapType {
            return entries.find { it.value == type } ?: throw Exception("맵 타입을 찾을 수 없습니다")
        }
    }
}