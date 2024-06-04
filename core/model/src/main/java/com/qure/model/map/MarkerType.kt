package com.qure.model.map

enum class MarkerType(val value: String) {
    MEMO("메모"),
    ENTIRE("낚시터 전체"),
    SEA("바다"),
    RESERVOIR("저수지"),
    FLATLAND("평지"),
    OTHER("기타");

    companion object {
        fun getMarkerType(type: String): MarkerType {
            return entries.find { it.value == type } ?: throw Exception("존재 하지 않는 타입입니다.")
        }
    }
}
