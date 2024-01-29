package com.qure.data.datasource.fishingspot

import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import com.qure.data.api.FishingSpotService
import com.qure.data.entity.fishingspot.FishingSpotEntity
import com.qure.domain.entity.fishingspot.FishingSpotQuery
import javax.inject.Inject

class FishingSpotRemoteDataSourceImpl
    @Inject
    constructor(
        private val fishingSpotService: FishingSpotService,
        private val buildPropertyRepository: BuildPropertyRepository,
    ) : FishingSpotRemoteDataSource {
        override suspend fun getFishingSopt(fishingSpotQuery: FishingSpotQuery): Result<List<FishingSpotEntity>> {
            return fishingSpotService.getFishingSpot(
                buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_PROJECT_ID),
                fishingSpotQuery,
            )
        }
    }
