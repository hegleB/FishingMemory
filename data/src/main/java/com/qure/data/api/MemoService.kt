package com.qure.data.api

import com.qure.data.entity.memo.MemoEntity
import com.qure.domain.entity.memo.MemoFieldsEntity
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface MemoService {

    @POST("/v1beta1/projects/{projectId}/databases/(default)/documents/memo")
    suspend fun postMemo(
        @Path("projectId") projectId: String,
        @Body fields: MemoFieldsEntity,
    ): Result<MemoEntity>

}