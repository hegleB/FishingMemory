package com.qure.domain.entity.memo

data class Memo(
    val name: String,
    val fields: MemoFieldsEntity?,
    val createTime: String,
    val updateTime: String,
)

data class MemoFieldsEntity(
    val fields: MemoFields
)

data class Document(
    val name: String,
    val fields: MemoFields,
    val createTime: String,
    val updateTime: String,
)

data class MemoFields(
    val email: FieldValue,
    val title: FieldValue,
    val image: FieldValue,
    val location: FieldValue,
    val date: FieldValue,
    val waterType: FieldValue,
    val fishType: FieldValue,
    val fishSize: FieldValue,
    val content: FieldValue,
    val createTime: FieldValue = FieldValue(System.currentTimeMillis().toString()),
)

data class FieldValue(
    val stringValue: String,
)
