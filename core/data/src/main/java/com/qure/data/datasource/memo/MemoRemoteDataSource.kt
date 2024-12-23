package com.qure.data.datasource.memo

import com.qure.data.entity.memo.MemoEntity
import com.qure.data.entity.memo.MemoQueryEntity
import com.qure.model.memo.MemoFields

internal interface MemoRemoteDataSource {
    suspend fun postMemo(memoFields: MemoFields): MemoEntity

    suspend fun postMemoQuery(): List<MemoQueryEntity>

    suspend fun deleteMemo(uuid: String): Unit

    suspend fun updateMemo(memoFields: MemoFields): MemoEntity
}
