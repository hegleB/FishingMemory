package com.qure.data.entity.fishingspot

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "fishingspot_table")
@Parcelize
data class FishingSpotBookmarkEntity(
    @PrimaryKey val number: Int,
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
) : Parcelable
