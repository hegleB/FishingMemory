package com.qure.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.qure.data.entity.weather.WeatherLocalEntity

@Dao
internal interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weatherLocalEntity: WeatherLocalEntity)


    @Query("select * from weather_table")
    suspend fun getWeather(): List<WeatherLocalEntity>

    @Query("select MIN(id) from weather_table")
    suspend fun getMinId(): Int?

    @Query("delete from weather_table where id = :id")
    suspend fun deleteWeatherById(id: Int)
}
