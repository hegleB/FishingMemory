package com.qure.domain.repository

import com.qure.domain.entity.memo.Memo
import com.qure.domain.entity.memo.MemoFields
import com.qure.domain.entity.memo.MemoQuery
import com.qure.domain.entity.memo.MemoStorage
import kotlinx.coroutines.flow.Flow
import java.io.File

interface MemoRepository {
    suspend fun createMemo(memoFields: MemoFields): Result<Memo>
    suspend fun uploadMemoImage(image: File): Result<MemoStorage>
    fun deleteMemo(uuid: String): Flow<Result<Unit>>
    fun getfilteredMemo(memoQuery: MemoQuery): Flow<Result<List<Memo>>>
}