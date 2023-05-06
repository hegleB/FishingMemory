package com.qure.domain.usecase.darkmode

import com.qure.domain.repository.DarkModeRepository
import javax.inject.Inject

class GetDarkModeUseCase @Inject constructor(
    private val darkModeRepository: DarkModeRepository,
) {
    operator suspend fun invoke(): String {
        return darkModeRepository.getDarkMode()
    }
}