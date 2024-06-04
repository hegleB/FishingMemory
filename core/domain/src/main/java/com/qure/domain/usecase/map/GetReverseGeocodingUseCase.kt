package com.qure.domain.usecase.map

import com.qure.data.repository.map.MapRepository
import javax.inject.Inject

class GetReverseGeocodingUseCase
    @Inject
    constructor(
        private val mapRepository: MapRepository,
    ) {
        operator fun invoke(coords: String) = mapRepository.getReverseGeocoding(coords)
    }
