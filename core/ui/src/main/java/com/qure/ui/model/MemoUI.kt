package com.qure.ui.model

import com.qure.model.extensions.Comma
import com.qure.model.extensions.Empty
import com.qure.model.memo.Document
import com.qure.model.memo.FieldStringValue
import com.qure.model.memo.Memo
import com.qure.model.memo.MemoFields
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ted.gun0912.clustering.clustering.TedClusterItem
import ted.gun0912.clustering.geometry.TedLatLng

@Serializable
data class MemoUI(
    val uuid: String = String.Empty,
    val name: String = String.Empty,
    val email: String = String.Empty,
    val title: String = String.Empty,
    val image: String = String.Empty,
    val location: String = String.Empty,
    val date: String = String.Empty,
    val waterType: String = String.Empty,
    val fishType: String = String.Empty,
    val fishSize: String = String.Empty,
    val content: String = String.Empty,
    val createTime: String? = System.currentTimeMillis().toString(),
    val coords: String = String.Empty,
) {
    val isValidMemo = title.isNotEmpty() &&
            image.isNotEmpty() &&
            waterType.isNotEmpty() &&
            fishSize.isNotEmpty() &&
            location.isNotEmpty() &&
            date.isNotEmpty() &&
            fishType.isNotEmpty() &&
            content.isNotEmpty()
}


fun MemoUI.toMemoString(): String {
    return Json.encodeToString(this)
}

fun String.toMemoUI(): MemoUI {
    return Json.decodeFromString(this)
}

fun MemoUI.toTedClusterItem(): TedClusterItem {
    val (lat, lng) = this.coords.split(String.Comma).map { it.toDouble() }
    return object : TedClusterItem {
        override fun getTedLatLng(): TedLatLng {
            return TedLatLng(lat, lng)
        }
    }
}

fun MemoUI.toMemoFields(email: String): MemoFields {
    return MemoFields(
        uuid = FieldStringValue(this.uuid),
        email = FieldStringValue(email),
        title = FieldStringValue(this.title),
        image = FieldStringValue(this.image),
        waterType = FieldStringValue(this.waterType),
        fishType = FieldStringValue(this.fishType),
        location = FieldStringValue(this.location),
        date = FieldStringValue(this.date),
        fishSize = FieldStringValue(this.fishSize),
        content = FieldStringValue(this.content),
        coords = FieldStringValue(this.coords),
    )
}


fun Memo.toMemoUI(): MemoUI {
    val data = this.fields.fields
    return MemoUI(
        uuid = data.uuid.stringValue,
        name = this.name,
        email = data.email.stringValue,
        title = data.title.stringValue,
        image = data.image.stringValue,
        location = data.location.stringValue,
        date = data.date.stringValue,
        waterType = data.waterType.stringValue,
        fishType = data.fishType.stringValue,
        fishSize = data.fishSize.stringValue,
        content = data.content.stringValue,
        createTime = data.createTime.stringValue,
        coords = data.coords.stringValue,
    )
}

fun Document.toMemoUI(): MemoUI {
    val data = this.fields
    return MemoUI(
        uuid = data.uuid.stringValue,
        name = this.name,
        email = data.email.stringValue,
        title = data.title.stringValue,
        image = data.image.stringValue,
        location = data.location.stringValue,
        date = data.date.stringValue,
        waterType = data.waterType.stringValue,
        fishType = data.fishType.stringValue,
        fishSize = data.fishSize.stringValue,
        content = data.content.stringValue,
        createTime = data.createTime.stringValue,
        coords = data.coords.stringValue,
    )
}
