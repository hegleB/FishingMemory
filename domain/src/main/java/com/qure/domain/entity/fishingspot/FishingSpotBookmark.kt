package com.qure.domain.entity.fishingspot

data class FishingSpotBookmark(
    val number: Int,
    val address: String,
    val data_base_date: String,
    val fish_type: String,
    val latitude: Double,
    val fishing_ground_type: String,
    val fishing_spot_name: String,
    val main_point: String,
    val road_address: String,
    val longitude: Double,
    val phone_number: String,
    val fee: String,
)
