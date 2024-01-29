package com.qure.domain.entity.fishingspot

data class FishingSpot(
    val document: Document,
    val readTime: String,
)

data class Document(
    val name: String,
    val fields: Fields,
    val createTime: String,
    val updateTime: String,
)

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

data class IntegerValue(
    val integerValue: Int,
)

data class StringValue(
    val stringValue: String,
)

data class DoubleValue(
    val doubleValue: Double,
)
