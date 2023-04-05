package com.qure.domain.usecase.geocoding

import com.qure.domain.entity.geocoding.Geocoding
import com.qure.domain.repository.GeocodingRepository
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class GetGeocodingUseCase @Inject constructor(
    private val geocodingRepository: GeocodingRepository,
) {
    operator fun invoke(query: String): Flow<Result<Geocoding>> =
        geocodingRepository.getGeocoding(query)

}