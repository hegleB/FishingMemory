package com.qure.data.utils

import androidx.room.TypeConverter
import com.qure.model.memo.MemoFieldsEntity
import com.qure.model.weather.Response
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter
    fun fromMemoFieldEntity(memoFields: MemoFieldsEntity): String {
        return Json.encodeToString(memoFields)
    }

    @TypeConverter
    fun toMemoFieldsEntity(memoFieldsString: String): MemoFieldsEntity {
        return Json.decodeFromString(memoFieldsString)
    }
}