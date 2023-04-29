package com.qure.map.model

import android.os.Parcelable
import com.qure.core.extensions.Empty
import com.qure.domain.entity.fishingspot.FishingSpot
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
    val address: String = String.Empty,
    val data_base_date: String = String.Empty,
    val fish_type: String = String.Empty,
    val main_point: String = String.Empty,
    val fishing_spot_name: String = String.Empty,
): Parcelable

fun FishingSpotUI.toTedClusterItem(): TedClusterItem {
    val lat = this.latitude
    val lng = this.longitude
    return object : TedClusterItem {
        override fun getTedLatLng(): TedLatLng {
            return TedLatLng(lat, lng)
        }
    }
}


fun FishingSpot.toFishingSpotUI(): FishingSpotUI {
    val data = this.document.fields
    return FishingSpotUI(
        road_address = data.road_address.stringValue,
        number = data.number.integerValue,
        latitude = data.latitude.doubleValue,
        longitude = data.longitude.doubleValue,
        fishing_ground_type = data.fishing_ground_type.stringValue,
        address = data.address.stringValue,
        data_base_date = data.data_base_date.stringValue,
        fish_type = data.fish_type.stringValue,
        main_point = data.main_point.stringValue,
        fishing_spot_name = data.fishing_spot_name.stringValue,
    )
}
