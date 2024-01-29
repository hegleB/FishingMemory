package com.qure.data.mapper

import com.qure.data.entity.fishingspot.FishingSpotBookmarkEntity
import com.qure.domain.entity.fishingspot.FishingSpotBookmark

fun FishingSpotBookmarkEntity.toFishingSpotBookmark(): FishingSpotBookmark {
    return FishingSpotBookmark(
        number = this.number,
        address = this.address,
        data_base_date = this.data_base_date,
        fish_type = this.fish_type,
        latitude = this.latitude,
        fishing_ground_type = this.fishing_ground_type,
        fishing_spot_name = this.fishing_spot_name,
        main_point = this.main_point,
        road_address = this.road_address,
        longitude = this.longitude,
        phone_number = this.phone_number,
        fee = this.fee,
    )
}

fun FishingSpotBookmark.toFishingSpotBookmarkEntity(): FishingSpotBookmarkEntity {
    return FishingSpotBookmarkEntity(
        number = this.number,
        address = this.address,
        data_base_date = this.data_base_date,
        fish_type = this.fish_type,
        latitude = this.latitude,
        fishing_ground_type = this.fishing_ground_type,
        fishing_spot_name = this.fishing_spot_name,
        main_point = this.main_point,
        road_address = this.road_address,
        longitude = this.longitude,
        phone_number = this.phone_number,
        fee = this.fee,
    )
}
