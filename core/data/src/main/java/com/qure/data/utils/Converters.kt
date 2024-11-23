package com.qure.data.utils

import androidx.room.TypeConverter
import com.qure.model.fishingspot.Document
import com.qure.model.memo.MemoFields
import com.qure.model.weather.Response
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter
    fun fromMemoFieldEntity(memoFields: MemoFields): String {
        return Json.encodeToString(memoFields)
    }

    @TypeConverter
    fun toMemoFieldsEntity(memoFieldsString: String): MemoFields {
        return Json.decodeFromString(memoFieldsString)
    }

    @TypeConverter
    fun fromWeatherResponse(response: Response): String {
        return Json.encodeToString(response)
    }

    @TypeConverter
    fun toWeatherResponse(responseString: String): Response {
        return Json.decodeFromString(responseString)
    }

    @TypeConverter
    fun fromFishingSpotDocument(document: Document): String {
        return Json.encodeToString(document)
    }

    @TypeConverter
    fun toFishingSpotDocument(documentString: String): Document {
        return Json.decodeFromString(documentString)
    }

}