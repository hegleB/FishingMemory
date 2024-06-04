package com.qure.domain.usecase.darkmode

import com.qure.data.repository.darkmode.DarkModeRepository
import javax.inject.Inject

class SetDarkModeUseCase
    @Inject
    constructor(
        private val darkModeRepository: DarkModeRepository,
    ) {
        suspend operator fun invoke(isDarkMode: String) {
            darkModeRepository.setDarkMode(isDarkMode)
        }
    }
