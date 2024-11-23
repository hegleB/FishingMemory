package com.qure.model.memo

import kotlinx.serialization.Serializable

@Serializable
data class Memo(
    val fields: MemoFields = MemoFields.EMPTY,
    val createTime: String,
)

@Serializable
data class MemoFieldsEntity(
    val fields: MemoFields = MemoFields.EMPTY,
) {
    companion object {
        val EMPTY = MemoFieldsEntity(
            fields = MemoFields.EMPTY,
        )
    }
}

@Serializable
data class Document(
    val fields: MemoFields,
    val createTime: String = "",
) {
    companion object {
        val EMPTY =
            Document(
                fields = MemoFields.EMPTY,
                createTime = "",
            )
    }
}

@Serializable
data class MemoFields(
    val uuid: FieldStringValue,
    val email: FieldStringValue,
    val title: FieldStringValue,
    val image: FieldStringValue,
    val location: FieldStringValue,
    val date: FieldStringValue,
    val waterType: FieldStringValue,
    val fishType: FieldStringValue,
    val fishSize: FieldStringValue,
    val content: FieldStringValue,
    val createTime: FieldStringValue = FieldStringValue(System.currentTimeMillis().toString()),
    val coords: FieldStringValue,
) {
    companion object {
        val EMPTY =
            MemoFields(
                uuid = FieldStringValue.EMPTY,
                email = FieldStringValue.EMPTY,
                title = FieldStringValue.EMPTY,
                image = FieldStringValue.EMPTY,
                location = FieldStringValue.EMPTY,
                date = FieldStringValue.EMPTY,
                waterType = FieldStringValue.EMPTY,
                fishType = FieldStringValue.EMPTY,
                fishSize = FieldStringValue.EMPTY,
                content = FieldStringValue.EMPTY,
                createTime = FieldStringValue.EMPTY,
                coords = FieldStringValue.EMPTY,
            )
    }
}

@Serializable
data class FieldStringValue(
    val stringValue: String = "",
) {
    companion object {
        val EMPTY = FieldStringValue("")
    }
}

data class FieldIntegerValue(
    val integerValue: Int,
)

data class FieldDoubleValue(
    val doubleValue: Double,
)
