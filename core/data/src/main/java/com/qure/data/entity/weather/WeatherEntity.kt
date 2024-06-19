package com.qure.data.entity.weather

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.qure.model.weather.Response
import kotlinx.serialization.Serializable

@Serializable
data class WeatherEntity(
    val response: Response,
)

@Serializable
@Entity(tableName = "weather_table")
data class WeatherLocalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val response: Response,
)