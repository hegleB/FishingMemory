package com.qure.model

import android.os.Parcelable
import com.qure.core.extensions.Empty
import com.qure.domain.entity.fishingspot.FishingSpot
import com.qure.domain.entity.fishingspot.FishingSpotBookmark
import kotlinx.android.parcel.Parcelize
import ted.gun0912.clustering.clustering.TedClusterItem
import ted.gun0912.clustering.geometry.TedLatLng

@Parcelize
data class FishingSpotUI(
    val road_address: String = String.Empty,
    val number: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val fishing_ground_type: String = String.Empty,
    val number_address: String = String.Empty,
    val data_base_date: String = String.Empty,
    val fish_type: String = String.Empty,
    val main_point: String = String.Empty,
    val fishing_spot_name: String = String.Empty,
    val fee: String = String.Empty,
    val phone_number: String = String.Empty,
) : Parcelable

fun FishingSpotUI.toTedClusterItem(): TedClusterItem {
    val lat = this.latitude
    val lng = this.longitude
    return object : TedClusterItem {
        override fun getTedLatLng(): TedLatLng {
            return TedLatLng(lat, lng)
        }
    }
}

fun FishingSpotUI.toFishingSpotBookmark(): FishingSpotBookmark {
    return FishingSpotBookmark(
        number = number,
        address = this.number_address,
        data_base_date = data_base_date,
        fish_type = fish_type,
        latitude = latitude,
        fishing_ground_type = fishing_ground_type,
        fishing_spot_name = fishing_spot_name,
        main_point = main_point,
        road_address = road_address,
        longitude = longitude,
        phone_number = phone_number,
        fee = fee,
    )
}

fun FishingSpotBookmark.toFishingSpotUI(): FishingSpotUI {
    return FishingSpotUI(
        number = this.number,
        number_address = this.address,
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

fun FishingSpot.toFishingSpotUI(): FishingSpotUI {
    val data = this.document.fields
    return FishingSpotUI(
        road_address = data.road_address.stringValue,
        number = data.number.integerValue,
        latitude = data.latitude.doubleValue,
        longitude = data.longitude.doubleValue,
        fishing_ground_type = data.fishing_ground_type.stringValue,
        number_address = data.address.stringValue,
        data_base_date = data.data_base_date.stringValue,
        fish_type = data.fish_type.stringValue,
        main_point = data.main_point.stringValue,
        fishing_spot_name = data.fishing_spot_name.stringValue,
        fee = data.fee.stringValue,
        phone_number = data.phone_number.stringValue,
    )
}
