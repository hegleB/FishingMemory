package com.qure.data.entity.memo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.qure.model.memo.Document
import com.qure.model.memo.MemoFields
import kotlinx.serialization.Serializable

@Serializable
data class MemoEntity(
    val name: String,
    val fields: MemoFields = MemoFields.EMPTY,
    val createTime: String,
    val updateTime: String,
)

@Serializable
@Entity(tableName = "fishing_memo_table")
data class MemoLocalEntity(
    @PrimaryKey val uuid: String,
    val fields: MemoFields = MemoFields.EMPTY,
    val createTime: String,
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
