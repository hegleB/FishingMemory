package com.qure.data.mapper

import com.qure.data.entity.memo.MemoEntity
import com.qure.domain.entity.memo.Memo

fun MemoEntity.toMemo(): Memo {
    val data = this

    return Memo(
        name = data.name,
        fields = data.MemoFields,
        createTime = data.createTime,
        updateTime = data.updateTime,
    )
}