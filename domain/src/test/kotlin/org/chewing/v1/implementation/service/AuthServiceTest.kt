package org.chewing.v1.implementation.service

import org.chewing.v1.implementation.TestDataFactory
import org.chewing.v1.implementation.auth.*
import org.chewing.v1.implementation.user.UserAppender
import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.service.AuthService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class AuthServiceTest(
) {
    private val authChecker: AuthChecker = mock()
    private val pushTokenProcessor: PushTokenProcessor = mock()
    private val jwtTokenProvider: JwtTokenProvider = mock()
    private val authReader: AuthReader = mock()
    private val authAppender: AuthAppender = mock()
    private val authRemover: AuthRemover = mock()
    private val userReader: UserReader = mock()
    private val userAppender: UserAppender = mock()
    private val authUpdater: AuthUpdater = mock()
    private val authSender: AuthSender = mock()
    private val authService: AuthService = AuthService(
        authChecker,
        pushTokenProcessor,
        jwtTokenProvider,
        authReader,
        authAppender,
        authRemover,
        userReader,
        userAppender,
        authUpdater,
        authSender
    )

    @Test
    @DisplayName("전화번호 인증 요청 테스트")
    fun `test sendPhoneVerification`() {
        val phoneNumber = TestDataFactory.createPhoneNumber()
        val verificationCode = TestDataFactory.createVerificationCode()

        whenever(authUpdater.updatePhoneVerificationCode(phoneNumber)).thenReturn(verificationCode)

        authService.sendPhoneVerification(phoneNumber)

        verify(authAppender).appendPhone(phoneNumber)
        verify(authSender).sendPhoneVerificationCode(phoneNumber, verificationCode)
    }
    @Test
    @DisplayName("이메일 인증 및 로그인 테스트")
    fun `test sendEmailVerification`() {
        val emailAddress = TestDataFactory.createEmailAddress()
        val verificationCode = TestDataFactory.createVerificationCode()

        whenever(authUpdater.updateEmailVerificationCode(emailAddress)).thenReturn(verificationCode)

        authService.sendEmailVerification(emailAddress)

        verify(authAppender).appendEmail(emailAddress)
        verify(authSender).sendEmailVerificationCode(emailAddress, verificationCode)
    }

    @Test
    @DisplayName("전화번호 인증 및 로그인 테스트")
    fun

}