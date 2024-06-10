package com.qure.data.api

import com.qure.data.entity.fishingspot.FishingSpotEntity
import com.qure.model.fishingspot.FishingSpotQuery
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

internal interface FishingSpotService {
    @POST("/v1beta1/projects/{projectId}/databases/(default)/documents:runQuery")
    suspend fun getFishingSpot(
        @Path("projectId") projectId: String,
        @Body fishingSpotQuery: FishingSpotQuery,
    ): List<FishingSpotEntity>
}
