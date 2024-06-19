package com.qure.data.datasource.fishingspot

import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import com.qure.data.api.FishingSpotService
import com.qure.data.entity.fishingspot.FishingSpotEntity
import javax.inject.Inject

internal class FishingSpotRemoteDataSourceImpl
@Inject
constructor(
    private val fishingSpotService: FishingSpotService,
    private val buildPropertyRepository: BuildPropertyRepository,
) : FishingSpotRemoteDataSource {
    override suspend fun getFishingSpots(collectionId: String): List<FishingSpotEntity> {
        var pageToken: String? = null
        val allFishingSpots = mutableListOf<FishingSpotEntity>()

        do {
            val response = fishingSpotService.getFishingSpots(
                buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_PROJECT_ID),
                collectionId,
                pageToken
            )
            val fishingSpots = response.documents.map { FishingSpotEntity(it) }
            allFishingSpots.addAll(fishingSpots)
            pageToken = response.nextPageToken
        } while (pageToken != null)
        return allFishingSpots
    }
}
