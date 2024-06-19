package com.qure.data.api

import com.qure.data.entity.fishingspot.FishingSpotsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface FishingSpotService {
    @GET("/v1/projects/{projectId}/databases/(default)/documents/{collectionId}")
    suspend fun getFishingSpots(
        @Path("projectId") projectId: String,
        @Path("collectionId") collectionId: String,
        @Query("pageToken") nextPageToken: String? = null,
    ): FishingSpotsResponse
}
