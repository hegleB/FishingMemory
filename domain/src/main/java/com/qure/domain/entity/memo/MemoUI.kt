package com.qure.domain.entity.memo

data class Memo(
    val name: String,
    val fields: MemoFieldsEntity?,
    val createTime: String,
    val updateTime: String,
)

data class MemoFieldsEntity(
    val fields: MemoFields,
)

data class Document(
    val name: String,
    val fields: MemoFields,
    val createTime: String,
    val updateTime: String,
) {
    companion object {
        val EMPTY =
            Document(
                name = "",
                fields = MemoFields.EMPTY,
                createTime = "",
                updateTime = "",
            )
    }
}

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

data class FieldStringValue(
    val stringValue: String,
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
