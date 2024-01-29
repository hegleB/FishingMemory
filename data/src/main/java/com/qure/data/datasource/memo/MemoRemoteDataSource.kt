package com.qure.data.datasource.memo

import com.qure.data.entity.memo.MemoEntity
import com.qure.data.entity.memo.MemoQueryEntity
import com.qure.data.entity.memo.UpdatedMemoEntity
import com.qure.domain.entity.memo.MemoFields
import com.qure.domain.entity.memo.MemoQuery

interface MemoRemoteDataSource {
    suspend fun postMemo(memoFields: MemoFields): Result<MemoEntity>

    suspend fun postMemoQuery(memoQuery: MemoQuery): Result<List<MemoQueryEntity>>

    suspend fun deleteMemo(uuid: String): Result<Unit>

    suspend fun updateMemo(memoFields: MemoFields): Result<UpdatedMemoEntity>
}
