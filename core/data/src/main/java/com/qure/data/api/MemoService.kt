package com.qure.data.api

import com.qure.data.entity.memo.MemoEntity
import com.qure.data.entity.memo.MemoQueryEntity
import com.qure.model.memo.MemoFieldsEntity
import com.qure.model.memo.MemoQuery
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

internal interface MemoService {
    @POST("/v1beta1/projects/{projectId}/databases/(default)/documents/memo")
    suspend fun postMemo(
        @Path("projectId") projectId: String,
        @Query("documentId") documentId: String,
        @Body fields: MemoFieldsEntity,
    ): MemoEntity

    @POST("/v1beta1/projects/{projectId}/databases/(default)/documents:runQuery")
    suspend fun postMemoFiltering(
        @Path("projectId") projectId: String,
        @Body memoQuery: MemoQuery,
    ): List<MemoQueryEntity>

    @DELETE("/v1beta1/projects/{projectId}/databases/(default)/documents/memo/{documentId}")
    suspend fun deleteMemo(
        @Path("projectId") projectId: String,
        @Path("documentId") documentId: String,
    )

    @PATCH("/v1beta1/projects/{projectId}/databases/(default)/documents/memo/{documentId}")
    suspend fun updateMemo(
        @Path("projectId") projectId: String,
        @Path("documentId") documentId: String,
        @Body fields: MemoFieldsEntity,
    ): MemoEntity
}
