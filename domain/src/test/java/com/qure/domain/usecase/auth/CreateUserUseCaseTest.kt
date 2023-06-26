package com.qure.domain.usecase.auth

import com.qure.domain.entity.auth.Email
import com.qure.domain.entity.auth.SignUpFields
import com.qure.domain.entity.auth.SignUpUser
import com.qure.domain.entity.auth.Token
import com.qure.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CreateUserUseCaseTest {

    private var authRepository: AuthRepository = mockk()
    private lateinit var createUserUseCase: CreateUserUseCase
    private val email = "test@example.com"
    private val socialToken = "social_token"

    @Before
    fun setup() {
        createUserUseCase = CreateUserUseCase(authRepository)

    }

    @Test
    fun `유저가 생성을 성공하여 유저 정보가 반환된다`() = runTest {
        // Given
        val token = "token"
        val signUpUser = SignUpUser(email, SignUpFields(Email(email), Token(token)), "20230101", "20230101")
        val result = Result.success(signUpUser)

        coEvery { authRepository.createUser(email, socialToken) } returns result
        coEvery { authRepository.saveTokenToLocal(token) } returns Unit
        coEvery { authRepository.saveEmailToLocal(email) } returns Unit

        // When
        val invokeResult = createUserUseCase(email, socialToken)

        // Then
        assertThat(invokeResult).isEqualTo(result)
        coVerify { authRepository.saveTokenToLocal(token) }
        coVerify { authRepository.saveEmailToLocal(email) }
    }

    @Test(expected = Exception::class)
    fun `유저 생성을 실패하여 예외가 발생한다`() = runTest {
        // Given
        val message = "user create exception"

        coEvery { authRepository.createUser(email, socialToken) } throws Exception(message)

        // When
       val result = createUserUseCase(email, socialToken)

        // Then
        assertThatThrownBy { result }.isInstanceOf(Exception::class.java)
            .hasMessage(message)
    }
}