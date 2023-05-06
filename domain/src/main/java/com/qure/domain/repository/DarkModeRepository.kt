package com.qure.domain.repository

interface DarkModeRepository {
    suspend fun setDarkMode(isDarkMode: String)
    suspend fun getDarkMode(): String
}