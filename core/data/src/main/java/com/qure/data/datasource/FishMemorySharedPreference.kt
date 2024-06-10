package com.qure.data.datasource

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.qure.data.utils.SHARED_PREFERNCE_KEY
import com.qure.data.utils.THEME_SYSTEM
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FishMemorySharedPreferenceImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : FishMemorySharedPreference {
        private val preference: SharedPreferences =
            context.getSharedPreferences(SHARED_PREFERNCE_KEY, Context.MODE_PRIVATE)

        override fun putString(
            key: String,
            value: String,
        ) {
            preference.edit(true) { putString(key, value) }
        }

        override fun getString(key: String): String {
            return preference.getString(key, "").toString()
        }

        override fun remove(key: String) {
            preference.edit(true) { remove(key) }
        }

        override fun putTheme(
            key: String,
            value: String,
        ) {
            preference.edit(true) { putString(key, value) }
        }

        override fun getTheme(key: String): String {
            return preference.getString(key, THEME_SYSTEM).toString()
        }
    }

internal interface FishMemorySharedPreference {
    fun putString(
        key: String,
        value: String,
    )

    fun getString(key: String): String

    fun remove(key: String)

    fun putTheme(
        key: String,
        value: String,
    )

    fun getTheme(key: String): String
}
