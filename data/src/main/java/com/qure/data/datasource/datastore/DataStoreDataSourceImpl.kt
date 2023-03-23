package com.qure.data.datasource.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataStoreDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
): DataStoreDataSource {
    override suspend fun readDataSource(key: String): String? {
        val dataStringKey = stringPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[dataStringKey]
    }

    override suspend fun writeDataSource(key: String, value: String) {
        val dataStringKey = stringPreferencesKey(key)
        context.dataStore.edit {
            it[dataStringKey] = value
        }
    }

    companion object {
        private const val DATASTORE_NAME = "FISING_MEMORY"
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)
    }
}