package com.qure.data.entity.memo

import com.qure.domain.entity.memo.Document
import com.qure.domain.entity.memo.MemoFields
import com.qure.domain.entity.memo.MemoFieldsEntity

data class MemoEntity(
    val name: String,
    val fields: MemoFieldsEntity,
    val createTime: String,
    val updateTime: String,
)

data class UpdatedMemoEntity(
    val name: String,
    val fields: MemoFields,
    val createTime: String,
    val updateTime: String,
)

data class MemoQueryEntity(
    val document: Document?,
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
