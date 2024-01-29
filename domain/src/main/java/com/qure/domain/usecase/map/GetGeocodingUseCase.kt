package com.qure.domain.usecase.map

import com.qure.domain.entity.map.Geocoding
import com.qure.domain.repository.MapRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGeocodingUseCase
    @Inject
    constructor(
        private val mapRepository: MapRepository,
    ) {
        operator fun invoke(query: String): Flow<Result<Geocoding>> = mapRepository.getGeocoding(query)
    }
