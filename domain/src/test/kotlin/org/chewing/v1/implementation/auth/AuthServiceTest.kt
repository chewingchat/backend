package org.chewing.v1.implementation.auth

import org.chewing.v1.external.ExternalAuthClient
import org.chewing.v1.implementation.TestDataFactory
import org.chewing.v1.implementation.user.UserProcessor
import org.chewing.v1.implementation.user.UserUpdater
import org.chewing.v1.model.auth.EmailAddress
import org.chewing.v1.model.auth.PhoneNumber
import org.chewing.v1.repository.AuthRepository
import org.chewing.v1.service.AuthService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


class AuthServiceTest {
    private val externalAuthClient: ExternalAuthClient = mock()
    private val authRepository: AuthRepository = mock()
    private val userProcessor: UserProcessor = mock()
    private val authReader: AuthReader = AuthReader(authRepository)
    private val authAppender: AuthAppender = AuthAppender(authRepository)
    private val authSender: AuthSender = AuthSender(externalAuthClient)
    private val authValidator: AuthValidator = mock()
    private val authProcessor: AuthProcessor = mock()
    private val userUpdater: UserUpdater = mock()
    private val authService = AuthService(
        userProcessor,
        authReader,
        authAppender,
        authSender,
        authValidator,
        authProcessor,
        userUpdater,
    )

    @Test
    fun makeEmailCredentialTest() {
        // Given
        // 이메일 인증 객체 생성
        val emailCredential = TestDataFactory.createEmailCredential()
        val verificationCode = TestDataFactory.createVerificationCode()
        // authRepository.generateVerificationCode() 메서드가 호출되면 인증 번호 생성후 리턴
        whenever(authRepository.generateVerificationCode(emailCredential)).thenReturn(verificationCode)
        // When
        authService.makeCredential(emailCredential)
        // Then
        // externalEmailClient.sendEmail() 메서드가 호출되면 인증 번호를 이메일로 전송
        verify(externalAuthClient).sendEmail(emailCredential as EmailAddress, verificationCode)
    }

    @Test
    fun makePhoneCredentialTest() {
        // Given
        val phoneCredential = TestDataFactory.createPhoneNumberCredential()
        val verificationCode = TestDataFactory.createVerificationCode()
        // authRepository.generateVerificationCode() 메서드가 호출되면 인증 번호 생성후 리턴
        whenever(authRepository.generateVerificationCode(phoneCredential)).thenReturn(verificationCode)
        // When
        authService.makeCredential(phoneCredential)
        // Then
        // externalPhoneClient.sendSms() 메서드가 호출되면 인증 번호를 SMS로 전송
        verify(externalAuthClient).sendSms(phoneCredential as PhoneNumber, verificationCode)
    }


}