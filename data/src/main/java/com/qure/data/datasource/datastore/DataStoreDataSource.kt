package com.qure.data.datasource.datastore

interface DataStoreDataSource {
    suspend fun readDataSource(key: String): String?

    suspend fun writeDataSource(
        key: String,
        value: String,
    )
}
