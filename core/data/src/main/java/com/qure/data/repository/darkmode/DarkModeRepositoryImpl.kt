package com.qure.data.repository.darkmode

import com.qure.data.utils.DARK_MODE_KEY
import com.qure.data.datasource.FishMemorySharedPreference
import javax.inject.Inject

class DarkModeRepositoryImpl
    @Inject
    constructor(
        private val fishingMemorySharedPreference: FishMemorySharedPreference,
    ) : DarkModeRepository {
        override suspend fun setDarkMode(isDarkMode: String) {
            fishingMemorySharedPreference.putTheme(DARK_MODE_KEY, isDarkMode)
        }

        override suspend fun getDarkMode(): String {
            return fishingMemorySharedPreference.getTheme(DARK_MODE_KEY)
        }
    }
