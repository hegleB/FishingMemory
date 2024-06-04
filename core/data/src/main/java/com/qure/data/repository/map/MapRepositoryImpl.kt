package com.qure.data.repository.map

import com.qure.data.datasource.map.MapRemoteDataSource
import com.qure.data.mapper.toGeocoding
import com.qure.data.mapper.toReverseGeocoding
import com.qure.model.map.Geocoding
import com.qure.model.map.ReverseGeocoding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MapRepositoryImpl
    @Inject
    constructor(
        private val mapRemoteDataSource: MapRemoteDataSource,
    ) : MapRepository {
        override fun getGeocoding(query: String): Flow<Geocoding> {
            return flow {
                emit(mapRemoteDataSource.getGeocoding(query).toGeocoding())
            }
        }

        override fun getReverseGeocoding(coords: String): Flow<ReverseGeocoding> {
            return flow {
                emit(mapRemoteDataSource.getReverseGeocoding(coords).toReverseGeocoding())
            }
        }
    }
