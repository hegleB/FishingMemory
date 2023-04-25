package com.qure.memo.model

import android.os.Parcelable
import com.qure.core.extensions.Empty
import com.qure.core.extensions.UUID
import com.qure.domain.entity.memo.Document
import com.qure.domain.entity.memo.Memo
import kotlinx.android.parcel.Parcelize

@Parcelize
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
    val coords: String = String.Empty
): Parcelable

fun Memo.toMemoUI(): MemoUI {
    val data = this.fields!!.fields
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
    )
}