package com.qure.domain.usecase.map

import com.qure.data.repository.map.MapRepository
import com.qure.model.map.Geocoding
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGeocodingUseCase
    @Inject
    constructor(
        private val mapRepository: MapRepository,
    ) {
        operator fun invoke(query: String): Flow<Geocoding> = mapRepository.getGeocoding(query)
    }
