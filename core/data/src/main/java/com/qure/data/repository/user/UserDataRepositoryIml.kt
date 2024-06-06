package com.qure.data.repository.user

import com.qure.model.darkmode.DarkModeConfig
import com.qure.model.user.SplashState
import com.qure.model.user.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


class UserDataRepositoryIml @Inject constructor() : UserDataRepository {

    override val userData: MutableStateFlow<UserData> = MutableStateFlow(UserData())
    override suspend fun setDarkModeConfig(darkModeConfig: DarkModeConfig) {
        userData.update { it.copy(darkModeConfig = darkModeConfig) }
    }

    override suspend fun setSplashState(splashState: SplashState) {
        userData.update {
            it.copy(splashState = splashState)
        }
    }

    override suspend fun setNewEmail(email: String) {
        userData.update { it.copy(email = email) }
    }

    override suspend fun setKakaoDeepLink(isKakaoDeepLink: Boolean) {
        userData.update { it.copy(isKakaoDeepLink = isKakaoDeepLink) }
    }
}