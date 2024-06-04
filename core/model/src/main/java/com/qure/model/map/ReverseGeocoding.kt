package com.qure.model.map

import kotlinx.serialization.Serializable

data class ReverseGeocoding(
    val status: Status,
    val results: List<Results>,
)

@Serializable
data class Status(
    val code: Int = 0,
    val name: String = "",
    val message: String = "",
)

@Serializable
data class Results(
    val name: String = "",
    val code: Code,
    val region: RegionEntity,
    val land: Land? = null,
)

@Serializable
data class Code(
    val id: String = "",
    val type: String = "",
    val mappingId: String = "",
)

@Serializable
data class RegionEntity(
    val area0: Area,
    val area1: Area,
    val area2: Area,
    val area3: Area,
    val area4: Area,
)

@Serializable
data class Land(
    val type: String = "",
    val name: String = "",
    val number1: String = "",
    val number2: String = "",
    val coords: Coords,
    val addition0: Addition,
    val addition1: Addition,
    val addition2: Addition,
    val addition3: Addition,
    val addition4: Addition,
)

@Serializable
data class Addition(
    val type: String  = "",
    val value: String  = "",
)

@Serializable
data class Area(
    val name: String = "",
    val coords: Coords,
    val alias: String = "",
)

@Serializable
data class Coords(
    val center: Center,
)

@Serializable
data class Center(
    val crs: String  = "",
    val x: Float = 0f,
    val y: Float = 0f,
)
