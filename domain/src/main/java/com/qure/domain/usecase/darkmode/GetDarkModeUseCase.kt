package com.qure.domain.usecase.darkmode

import com.qure.domain.repository.DarkModeRepository
import javax.inject.Inject

class GetDarkModeUseCase
    @Inject
    constructor(
        private val darkModeRepository: DarkModeRepository,
    ) {
        suspend operator fun invoke(): String {
            return darkModeRepository.getDarkMode()
        }
    }
