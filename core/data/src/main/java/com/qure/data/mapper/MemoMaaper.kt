package com.qure.data.mapper

import com.qure.data.entity.memo.MemoEntity
import com.qure.data.entity.memo.MemoLocalEntity
import com.qure.data.entity.memo.MemoQueryEntity
import com.qure.data.entity.memo.MemoStorageEntity
import com.qure.model.memo.Document
import com.qure.model.memo.Memo
import com.qure.model.memo.MemoFields
import com.qure.model.memo.MemoFieldsEntity
import com.qure.model.memo.MemoStorage

fun MemoEntity.toMemo(): Memo {
    val data = this

    return Memo(
        fields = data.fields,
        createTime = data.createTime,
    )
}

fun MemoEntity.toDocument(): Document {
    val data = this
    return Document(
        fields = data.fields,
        createTime = data.createTime,
    )
}

fun MemoQueryEntity.toMemo(): Memo {
    val data = this.document ?: Document.EMPTY
    return Memo(
        fields = data.fields,
        createTime = data.createTime,
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

fun MemoFields.toMemoLocalEntity(): MemoLocalEntity {
    return MemoLocalEntity(
        uuid = this.uuid.stringValue,
        fields = this,
        createTime = this.createTime.stringValue,
    )
}

fun MemoFields.toDocument(): Document {
    return Document(
        fields = this,
        createTime = this.createTime.stringValue,
    )
}

fun MemoLocalEntity.toMemo(): Memo {
    return Memo(
        fields = this.fields,
        createTime = this.createTime,
    )
}

fun Memo.toMemoLocalEntity(): MemoLocalEntity {
    return MemoLocalEntity(
        uuid = this.fields.uuid.stringValue,
        fields = this.fields,
        createTime = this.createTime,
    )
}

fun List<Memo>.toMemosLocalEntity(): List<MemoLocalEntity> {
    return this.map { it.toMemoLocalEntity() }
}