package com.qure.domain.repository

import com.qure.domain.entity.memo.Memo
import com.qure.domain.entity.memo.MemoFields

interface MemoRepository {

    suspend fun createMemo(memoFields: MemoFields): Result<Memo>
}