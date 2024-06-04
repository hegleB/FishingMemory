package com.qure.data.mapper

import com.qure.data.entity.memo.MemoEntity
import com.qure.data.entity.memo.MemoQueryEntity
import com.qure.data.entity.memo.MemoStorageEntity
import com.qure.data.entity.memo.UpdatedMemoEntity
import com.qure.model.memo.Document
import com.qure.model.memo.Memo
import com.qure.model.memo.MemoFields
import com.qure.model.memo.MemoFieldsEntity
import com.qure.model.memo.MemoStorage

fun MemoEntity.toMemo(): Memo {
    val data = this

    return Memo(
        name = data.name,
        fields = data.fields,
        createTime = data.createTime,
        updateTime = data.updateTime,
    )
}

fun UpdatedMemoEntity.toDocument(): Document {
    val data = this
    return Document(
        name = data.name,
        fields = data.fields,
        createTime = data.createTime,
        updateTime = data.updateTime,
    )
}

fun MemoQueryEntity.toMemo(): Memo {
    val data = this.document ?: Document.EMPTY
    return Memo(
        name = data.name,
        fields = data.fields.toMemoFieldsEntity(),
        createTime = data.createTime,
        updateTime = data.updateTime,
    )
}

fun MemoFields.toMemoFieldsEntity(): MemoFieldsEntity {
    return MemoFieldsEntity(
        fields = this,
    )
}

fun MemoStorageEntity.toMemoStorage(): MemoStorage {
    return MemoStorage(
        name = this.name,
        bucket = this.bucket,
        generation = this.generation,
        metageneration = this.metageneration,
        contentType = this.contentType,
        timeCreated = this.etag,
        updated = this.updated,
        storageClass = this.storageClass,
        size = this.size,
        md5Hash = this.md5Hash,
        contentEncoding = this.contentEncoding,
        contentDisposition = this.contentDisposition,
        crc32c = this.crc32c,
        etag = this.etag,
        downloadTokens = this.downloadTokens,
    )
}
