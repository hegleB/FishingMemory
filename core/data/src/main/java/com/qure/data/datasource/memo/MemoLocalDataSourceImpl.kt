package com.qure.data.datasource.memo

import com.qure.data.entity.memo.MemoLocalEntity
import com.qure.data.local.MemoDao
import javax.inject.Inject

internal class MemoLocalDataSourceImpl @Inject constructor(
    private val memoDao: MemoDao,
) : MemoLocalDataSource {
    override suspend fun insertMemo(memoLocalEntity: MemoLocalEntity) {
        memoDao.insertMemo(memoLocalEntity)
    }

    override suspend fun insertMemos(memosLocalEntity: List<MemoLocalEntity>) {
        memoDao.insertMemos(memosLocalEntity)
    }

    override suspend fun getMemos(): List<MemoLocalEntity> {
        return memoDao.getMemos()
    }

    override suspend fun deleteMemo(uuid: String) {
        memoDao.deleteMemo(uuid)
    }

    override suspend fun deleteAllMemos() {
        memoDao.deleteAllMemos()
    }

    override suspend fun updateMemo(memoLocalEntity: MemoLocalEntity) {
        memoDao.updateMemo(memoLocalEntity)
    }
}