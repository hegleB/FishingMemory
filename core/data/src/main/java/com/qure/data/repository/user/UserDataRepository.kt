package com.qure.data.repository.user

import com.qure.model.darkmode.DarkModeConfig
import com.qure.model.user.SplashState
import com.qure.model.user.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userData: Flow<UserData>

    suspend fun setDarkModeConfig(darkModeConfig: DarkModeConfig)
    suspend fun setSplashState(onboardingState: SplashState)
    suspend fun setNewEmail(email: String)
    suspend fun setKakaoDeepLink(isKakaoDeepLink: Boolean)
}