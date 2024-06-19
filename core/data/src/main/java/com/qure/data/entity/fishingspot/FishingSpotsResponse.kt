package com.qure.data.entity.fishingspot

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.qure.model.fishingspot.Document
import kotlinx.serialization.Serializable

@Serializable
data class FishingSpotsResponse(
    val documents: List<Document>,
    val nextPageToken: String? = null,
)

@Serializable
data class FishingSpotEntity(
    val document: Document,
)

@Serializable
@Entity(tableName = "fishing_spot_table")
data class FishingSpotLocalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val document: Document,
)