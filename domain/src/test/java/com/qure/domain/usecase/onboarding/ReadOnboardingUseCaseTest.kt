package com.qure.domain.usecase.onboarding

import com.qure.domain.entity.OnboardingType
import com.qure.domain.repository.OnboardingRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ReadOnboardingUseCaseTest {
    private lateinit var readOnboardingUseCase: ReadOnboardingUseCase
    private val onboardingRepository: OnboardingRepository = mockk()

    @Before
    fun setup() {
        readOnboardingUseCase = ReadOnboardingUseCase(onboardingRepository)
    }

    @Test
    fun `DataStore에 저장되어 있는 Onboarding 데이터를 읽는다`() =
        runTest {
            // Given
            coEvery { onboardingRepository.readOnboarding(OnboardingType.AFTER_SPLASH.key) } returns "IS_SHOWN"

            // When
            val result = readOnboardingUseCase.invoke(OnboardingType.AFTER_SPLASH)

            // Then
            assertThat(result).isEqualTo("IS_SHOWN")
        }
}
