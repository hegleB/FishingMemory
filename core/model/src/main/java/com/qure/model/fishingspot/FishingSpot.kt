package com.qure.model.fishingspot

import kotlinx.serialization.Serializable

data class FishingSpot(
    val document: Document,
)

@Serializable
data class Document(
    val name: String,
    val fields: Fields,
    val createTime: String,
    val updateTime: String,
)

@Serializable
data class Fields(
    val number: IntegerValue,
    val address: StringValue,
    val data_base_date: StringValue,
    val fish_type: StringValue,
    val latitude: DoubleValue,
    val fishing_ground_type: StringValue,
    val fishing_spot_name: StringValue,
    val main_point: StringValue,
    val road_address: StringValue,
    val longitude: DoubleValue,
    val phone_number: StringValue,
    val fee: StringValue,
)

@Serializable
data class IntegerValue(
    val integerValue: Int,
)

@Serializable
data class StringValue(
    val stringValue: String,
)

@Serializable
data class DoubleValue(
    val doubleValue: Double = 0.0,
)
