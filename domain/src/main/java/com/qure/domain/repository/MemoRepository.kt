package com.qure.domain.repository

import com.qure.domain.entity.memo.*
import kotlinx.coroutines.flow.Flow
import java.io.File

interface MemoRepository {
    suspend fun createMemo(memoFields: MemoFields): Result<Memo>
    suspend fun uploadMemoImage(image: File): Result<MemoStorage>
    fun getUpdatedMemo(memoFields: MemoFields): Flow<Result<Document>>
    fun deleteMemo(uuid: String): Flow<Result<Unit>>
    fun getfilteredMemo(memoQuery: MemoQuery): Flow<Result<List<Memo>>>

}