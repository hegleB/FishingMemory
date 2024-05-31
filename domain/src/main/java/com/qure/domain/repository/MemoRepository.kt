package com.qure.domain.repository

import com.qure.domain.entity.memo.Document
import com.qure.domain.entity.memo.Memo
import com.qure.domain.entity.memo.MemoFields
import com.qure.domain.entity.memo.MemoStorage
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
