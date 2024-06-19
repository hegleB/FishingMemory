package com.qure.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.qure.data.entity.memo.MemoLocalEntity

@Dao
internal interface MemoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemo(memoLocalEntity: MemoLocalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemos(memoLocalEntities: List<MemoLocalEntity>)

    @Query("select * from fishing_memo_table")
    suspend fun getMemos(): List<MemoLocalEntity>

    @Query("delete from fishing_memo_table where uuid = :uuid")
    suspend fun deleteMemo(uuid: String)

    @Query("delete from fishing_memo_table")
    suspend fun deleteAllMemos()

    @Update
    suspend fun updateMemo(memoLocalEntity: MemoLocalEntity)
}
