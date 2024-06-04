package com.qure.data.entity.memo

import com.qure.model.memo.Document
import com.qure.model.memo.MemoFields
import com.qure.model.memo.MemoFieldsEntity
import kotlinx.serialization.Serializable

@Serializable
data class MemoEntity(
    val name: String,
    val fields: MemoFieldsEntity,
    val createTime: String,
    val updateTime: String,
)

@Serializable
data class UpdatedMemoEntity(
    val name: String,
    val fields: MemoFields,
    val createTime: String,
    val updateTime: String,
)

@Serializable
data class MemoQueryEntity(
    val document: Document = Document.EMPTY,
    val readTime: String,
) {
    companion object {
        val EMPTY =
            MemoQueryEntity(
                document = Document.EMPTY,
                readTime = "",
            )
    }
}
