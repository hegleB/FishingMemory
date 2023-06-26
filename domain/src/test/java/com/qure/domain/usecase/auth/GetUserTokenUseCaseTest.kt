package com.qure.domain.usecase.auth

import com.qure.domain.entity.auth.Email
import com.qure.domain.entity.auth.SignUpFields
import com.qure.domain.entity.auth.SignUpUser
import com.qure.domain.entity.auth.Token
import com.qure.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test


class GetUserTokenUseCaseTest() {

    private var authRepository: AuthRepository = mockk()
    private lateinit var getUserTokenUseCase: GetUserTokenUseCase

    @Before
    fun setup() {
        getUserTokenUseCase = GetUserTokenUseCase(authRepository)
    }

    @Test
    fun `가입된 유저의 정보를 가져온다`() = runTest {
        // Given
        val email = "test@example.com"
        val token = "testToken"
        val signupUser = SignUpUser(
            email,
            SignUpFields(Email(email), Token(token)),
            "20230101",
            "20230101"
        )
        coEvery { authRepository.getSignedUpUser(email) } returns flowOf(Result.success(signupUser))

        // When
        val result = getUserTokenUseCase.invoke(email)

        // Then
        assertThat(result.first()).isEqualTo(Result.success(signupUser))
    }

    @Test
    fun `가입된 유저의 정보를 가져오는데 실패하여 예외가 발생한다`() = runTest {
        // Given
        val email = "test@example.com"
        val token = "testToken"
        val throwable = Throwable()
        coEvery { authRepository.getSignedUpUser(email) } returns flowOf(Result.failure(throwable))

        // When
        val result = getUserTokenUseCase.invoke(email)

        // Then
        assertThat(result.first()).isEqualTo(Result.failure<Throwable>(throwable))
    }
}