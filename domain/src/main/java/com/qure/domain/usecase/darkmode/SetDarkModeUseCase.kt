package com.qure.domain.usecase.darkmode

import com.qure.domain.repository.DarkModeRepository
import javax.inject.Inject

class SetDarkModeUseCase @Inject constructor(
    private val darkModeRepository: DarkModeRepository,
) {
    operator suspend fun invoke(isDarkMode: String) {
        darkModeRepository.setDarkMode(isDarkMode)
    }
}