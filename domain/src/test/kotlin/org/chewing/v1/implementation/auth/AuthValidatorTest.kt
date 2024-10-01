package org.chewing.v1.implementation.auth

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.implementation.TestDataFactory
import org.chewing.v1.implementation.user.UserChecker
import org.chewing.v1.repository.AuthRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock

class AuthValidatorTest {
    private val userChecker: UserChecker = mock()
    private val authRepository: AuthRepository = mock()
    private val authValidator = AuthValidator(authRepository, userChecker)

    @Test
    fun validateCodeTest() {
        // Given
        val scenarios = listOf(
            Pair(TestDataFactory.createValidPhoneContact(), TestDataFactory.createVerificationCode()),
            Pair(TestDataFactory.createValidEmailContact(), TestDataFactory.createVerificationCode())
        )
        // When
        scenarios.forEach { (contact, verificationCode) ->
            // Then
            // No exception should be thrown
            assertDoesNotThrow {
                authValidator.validateCode(contact, verificationCode)
            }
        }
    }
    @Test
    fun validateExpiredCodeTest() {
        // Given
        val scenarios = listOf(
            Pair(TestDataFactory.createExpiredPhoneContact(), TestDataFactory.createVerificationCode()),
            Pair(TestDataFactory.createExpiredEmailContact(), TestDataFactory.createVerificationCode())
        )
        // When
        scenarios.forEach { (contact, verificationCode) ->
            // Then
            // ConflictException with ErrorCode.VALIDATE_EXPIRED should be thrown
            val exception = assertThrows<ConflictException> {
                authValidator.validateCode(contact, verificationCode)
            }
            // 발생한 예외의 ErrorCode가 VALIDATE_EXPIRED인지 검증
            assertEquals(ErrorCode.VALIDATE_EXPIRED, exception.errorCode)
        }
    }
    @Test
    fun validateWrongCodeTest() {
        // Given
        val scenarios = listOf(
            Pair(TestDataFactory.createValidPhoneContact(), TestDataFactory.createWrongVerificationCode()),
            Pair(TestDataFactory.createValidEmailContact(), TestDataFactory.createWrongVerificationCode())
        )
        // When
        scenarios.forEach { (contact, verificationCode) ->
            // Then
            // ConflictException with ErrorCode.VALIDATE_WRONG should be thrown
            val exception = assertThrows<ConflictException> {
                authValidator.validateCode(contact, verificationCode)
            }
            // 발생한 예외의 ErrorCode가 VALIDATE_WRONG인지 검증
            assertEquals(ErrorCode.VALIDATE_WRONG, exception.errorCode)
        }
    }
}