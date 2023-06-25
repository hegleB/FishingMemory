package com.qure.splash

import com.example.testing.CoroutinesTestRule
import com.qure.core.extensions.Empty
import com.qure.domain.entity.OnboardingType
import com.qure.domain.repository.AuthRepository
import com.qure.domain.usecase.onboarding.ReadOnboardingUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class SplashViewModelTest {

    @get:Rule
    val croutineTestRule = CoroutinesTestRule()

    private lateinit var splashViewModel: SplashViewModel

    private var authRepository: AuthRepository = mockk()
    private var readOnboardingUseCase: ReadOnboardingUseCase = mockk()

    @Before
    fun setup() {
        splashViewModel = SplashViewModel(authRepository, readOnboardingUseCase)
    }

    @Test
    fun `카카오로그인 시 해당 아이디가 가입되어 있지 않다`() = runTest {
        // Given
        coEvery { authRepository.getAccessTokenFromLocal() } returns String.Empty

        // When
        val isSignUp = splashViewModel.isSignedUp()

        // Then
        assertThat(isSignUp).isFalse()
    }

    @Test
    fun `카카오로그인 시 해당 아이디가 가입되어 있다`() = runTest {
        // Given
        coEvery { authRepository.getAccessTokenFromLocal() } returns "123"

        // When
        val isSignUp = splashViewModel.isSignedUp()

        // Then
        assertThat(isSignUp).isTrue()
    }

    @Test
    fun `로그인 시 해당 아이디는 처음 방문한 유저이다`() = runTest {
        // Given
        coEvery { readOnboardingUseCase(OnboardingType.AFTER_SPLASH) } returns null

        // When
        splashViewModel.checkFirstVisitor()
        advanceUntilIdle()

        // Then
        assertThat(splashViewModel.isFirstVisitor.value).isTrue()
    }

    @Test
    fun `로그인 시 해당 아이디는 처음 방문한 유저가 아니다`() = runTest {
        // Given
        coEvery { readOnboardingUseCase(OnboardingType.AFTER_SPLASH) } returns "user"

        // When
        splashViewModel.checkFirstVisitor()
        advanceUntilIdle()

        // Then
        assertThat(splashViewModel.isFirstVisitor.value).isFalse()
    }
}