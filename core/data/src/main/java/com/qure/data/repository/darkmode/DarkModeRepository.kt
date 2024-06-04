package com.qure.data.repository.darkmode

interface DarkModeRepository {
    suspend fun setDarkMode(isDarkMode: String)

    suspend fun getDarkMode(): String
}
