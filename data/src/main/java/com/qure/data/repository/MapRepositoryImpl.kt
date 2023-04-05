package com.qure.data.repository

import com.qure.data.datasource.map.MapRemoteDataSource
import com.qure.data.mapper.toGeocoding
import com.qure.domain.entity.map.Geocoding
import com.qure.domain.repository.MapRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val mapRemoteDataSource: MapRemoteDataSource,
) : MapRepository {

    override fun getGeocoding(query: String): Flow<Result<Geocoding>> {
        return flow {
            mapRemoteDataSource.getGeocoding(query)
                .onSuccess { geocoding ->
                    emit(Result.success(geocoding.toGeocoding()))
                }
                .onFailure { throwable ->
                    emit(Result.failure(throwable))
                }
        }
    }
}