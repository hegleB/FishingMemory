package com.qure.domain.entity.memo

import com.qure.domain.entity.auth.Email

data class Memo(
    val name: String,
    val fields: MemoFieldsEntity?,
    val createTime: String,
    val updateTime: String,
)

data class MemoFieldsEntity(
    val fields: MemoFields
)

data class MemoFields(
    val email: Email,
    val title: MemoTitle,
    val image: MemoImage,
    val location: MemoLocation,
    val date: MemoDate,
    val fishType: FishType,
    val fishSize: FishSize,
    val content: MemoContent,
)

data class MemoTitle(
    val stringValue: String,
)

data class MemoImage(
    val stringValue: String,
)

data class MemoLocation(
    val stringValue: String,
)

data class MemoDate(
    val stringValue: String,
)

data class FishType(
    val stringValue: String,
)

data class MemoContent(
    val stringValue: String,
)

data class FishSize(
    val stringValue: String,
)