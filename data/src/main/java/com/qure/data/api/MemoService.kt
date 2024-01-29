package com.qure.data.api

import com.qure.data.entity.memo.MemoEntity
import com.qure.data.entity.memo.MemoQueryEntity
import com.qure.data.entity.memo.UpdatedMemoEntity
import com.qure.domain.entity.memo.*
import retrofit2.http.*

interface MemoService {
    @POST("/v1beta1/projects/{projectId}/databases/(default)/documents/memo")
    suspend fun postMemo(
        @Path("projectId") projectId: String,
        @Query("documentId") documentId: String,
        @Body fields: MemoFieldsEntity,
    ): Result<MemoEntity>

    @POST("/v1beta1/projects/{projectId}/databases/(default)/documents:runQuery")
    suspend fun postMemoFiltering(
        @Path("projectId") projectId: String,
        @Body memoQuery: MemoQuery,
    ): Result<List<MemoQueryEntity>>

    @DELETE("/v1beta1/projects/{projectId}/databases/(default)/documents/memo/{documentId}")
    suspend fun deleteMemo(
        @Path("projectId") projectId: String,
        @Path("documentId") documentId: String,
    ): Result<Unit>

    @PATCH("/v1beta1/projects/{projectId}/databases/(default)/documents/memo/{documentId}")
    suspend fun updateMemo(
        @Path("projectId") projectId: String,
        @Path("documentId") documentId: String,
        @Body fields: MemoFieldsEntity,
    ): Result<UpdatedMemoEntity>
}
