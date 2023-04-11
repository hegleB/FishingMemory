package com.qure.domain.repository

import com.qure.domain.entity.memo.Memo
import com.qure.domain.entity.memo.MemoFields
import com.qure.domain.entity.memo.MemoStorage
import java.io.File

interface MemoRepository {
    suspend fun createMemo(memoFields: MemoFields): Result<Memo>
    suspend fun uploadMemoImage(image: File): Result<MemoStorage>
}