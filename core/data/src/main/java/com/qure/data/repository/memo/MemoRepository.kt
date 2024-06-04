package com.qure.data.repository.memo

import com.qure.model.memo.Document
import com.qure.model.memo.Memo
import com.qure.model.memo.MemoFields
import com.qure.model.memo.MemoStorage
import kotlinx.coroutines.flow.Flow
import java.io.File

interface MemoRepository {
    suspend fun createMemo(memoFields: MemoFields): Memo

    suspend fun uploadMemoImage(image: File): MemoStorage

    fun getUpdatedMemo(memoFields: MemoFields): Flow<Document>

    fun deleteMemo(uuid: String): Flow<Unit>

    fun getfilteredMemo(): Flow<List<Memo>>

    fun deleteAllMemos(): Flow<Boolean>
}
