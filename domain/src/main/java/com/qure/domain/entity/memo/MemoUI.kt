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
) {
    companion object {
        val EMPTY = Document(
            name = "",
            fields = MemoFields.EMPTY,
            createTime = "",
            updateTime = "",
        )
    }
}

data class MemoFields(
    val uuid: FieldValue,
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
) {
    companion object {
        val EMPTY = MemoFields(
            uuid = FieldValue.EMPTY,
            email = FieldValue.EMPTY,
            title = FieldValue.EMPTY,
            image = FieldValue.EMPTY,
            location = FieldValue.EMPTY,
            date = FieldValue.EMPTY,
            waterType = FieldValue.EMPTY,
            fishType = FieldValue.EMPTY,
            fishSize = FieldValue.EMPTY,
            content = FieldValue.EMPTY,
            createTime = FieldValue.EMPTY,
        )
    }
}

data class FieldValue(
    val stringValue: String,
) {
    companion object {
        val EMPTY = FieldValue("")
    }
}
