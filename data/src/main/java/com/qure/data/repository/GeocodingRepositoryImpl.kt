package com.qure.data.repository

import com.qure.data.datasource.geocoding.GeocodingRemoteDataSource
import com.qure.data.mapper.toGeocoding
import com.qure.domain.entity.geocoding.Geocoding
import com.qure.domain.repository.GeocodingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GeocodingRepositoryImpl @Inject constructor(
    private val geocodingRemoteDataSource: GeocodingRemoteDataSource,
) : GeocodingRepository {

    override fun getGeocoding(query: String): Flow<Result<Geocoding>> {
        return flow {
            geocodingRemoteDataSource.getGeocoding(query)
                .onSuccess { geocoding ->
                    emit(Result.success(geocoding.toGeocoding()))
                }
                .onFailure { throwable ->
                    emit(Result.failure(throwable))
                }
        }
    }
}