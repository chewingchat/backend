package org.chewing.v1.service

import org.chewing.v1.TestDataFactory
import org.chewing.v1.error.AuthorizationException
import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.external.ExternalAuthClient
import org.chewing.v1.implementation.auth.*
import org.chewing.v1.repository.EmailRepository
import org.chewing.v1.repository.LoggedInRepository
import org.chewing.v1.repository.PhoneRepository
import org.chewing.v1.repository.UserRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@DisplayName("AuthService 테스트")
class AuthServiceTest {
    private val phoneRepository: PhoneRepository = mock()
    private val emailRepository: EmailRepository = mock()
    private val loggedInRepository: LoggedInRepository = mock()
    private val userRepository: UserRepository = mock()
    private val externalAuthClient: ExternalAuthClient = mock()
    private val authReader: AuthReader = AuthReader(phoneRepository, emailRepository, loggedInRepository)
    private val authAppender: AuthAppender = AuthAppender(loggedInRepository, emailRepository, phoneRepository)
    private val authValidator: AuthValidator = AuthValidator(userRepository, phoneRepository, emailRepository)
    private val authUpdater: AuthUpdater = AuthUpdater(loggedInRepository)
    private val jwtTokenProvider: JwtTokenProvider = JwtTokenProvider(
        "mysecretkey12345asdfvasdfvhjaaaaaaaaaaaaaaaaaaaaaaaaaslfdjasdlkr231243123412",
        1000L * 60 * 60 * 24 * 7,
        1000L * 60 * 60 * 24 * 30
    )
    private val authRemover: AuthRemover = AuthRemover(loggedInRepository)
    private val authSender: AuthSender = AuthSender(externalAuthClient)

    private val authService: AuthService = AuthService(
        authReader,
        authAppender,
        authSender,
        authValidator,
        authUpdater,
        jwtTokenProvider,
        authRemover
    )

    @Test
    fun 이메일_인증_생성() {
        val emailAddress = TestDataFactory.createEmailAddress()
        val verificationCode = "1234"
        whenever(emailRepository.appendIfNotExists(emailAddress)).thenReturn(verificationCode)

        authService.createCredential(emailAddress)

        verify(emailRepository).appendIfNotExists(emailAddress)

        verify(externalAuthClient).sendEmail(emailAddress, verificationCode)
    }

    @Test
    fun 전화번호_인증_생성() {
        val phoneNumber = TestDataFactory.createPhoneNumber()
        val verificationCode = "1234"
        whenever(phoneRepository.appendIfNotExists(phoneNumber)).thenReturn(verificationCode)

        authService.createCredential(phoneNumber)

        verify(phoneRepository).appendIfNotExists(phoneNumber)
        verify(externalAuthClient).sendSms(phoneNumber, verificationCode)
    }

    @Test
    fun 이메일_인증_검증() {
        val emailAddress = TestDataFactory.createEmailAddress()
        val verificationCode = "1234"
        val email = TestDataFactory.createEmail(verificationCode)
        whenever(emailRepository.read(emailAddress)).thenReturn(email)

        val result = authService.verify(emailAddress, verificationCode)

        assert(result == email)
    }

    @Test
    fun 이메일_인증번호가_틀려야_한다() {
        val emailAddress = TestDataFactory.createEmailAddress()
        val verificationCode = "1234"
        val email = TestDataFactory.createEmail("4321")
        whenever(emailRepository.read(emailAddress)).thenReturn(email)

        val exception = assertThrows<ConflictException> {
            authService.verify(emailAddress, verificationCode)
        }
        assert(exception.errorCode == ErrorCode.WRONG_VALIDATE_CODE)
    }

    @Test
    fun 이메일_인증번호가_만료되어야_한다() {
        val emailAddress = TestDataFactory.createEmailAddress()
        val verificationCode = "1234"
        val email = TestDataFactory.createExpiredEmail(verificationCode)
        whenever(emailRepository.read(emailAddress)).thenReturn(email)

        val exception = assertThrows<ConflictException> {
            authService.verify(emailAddress, verificationCode)
        }

        assert(exception.errorCode == ErrorCode.EXPIRED_VALIDATE_CODE)
    }

    @Test
    fun 전화번호_인증_검증() {
        val phoneNumber = TestDataFactory.createPhoneNumber()
        val verificationCode = "1234"
        val phone = TestDataFactory.createPhone(verificationCode)
        whenever(phoneRepository.read(phoneNumber)).thenReturn(phone)

        val result = authService.verify(phoneNumber, verificationCode)

        assert(result == phone)
    }

    @Test
    fun 전화번호_인증번호가_틀려야_한다() {
        val phoneNumber = TestDataFactory.createPhoneNumber()
        val verificationCode = "1234"
        val phone = TestDataFactory.createPhone("4321")
        whenever(phoneRepository.read(phoneNumber)).thenReturn(phone)

        val exception = assertThrows<ConflictException> {
            authService.verify(phoneNumber, verificationCode)
        }
        assert(exception.errorCode == ErrorCode.WRONG_VALIDATE_CODE)
    }

    @Test
    fun `이메일 인증시 인증요청을 하지 않고 잘몰된 접근 한 경우`() {
        val emailAddress = TestDataFactory.createEmailAddress()
        val verificationCode = "1234"
        whenever(emailRepository.read(emailAddress)).thenReturn(null)

        val exception = assertThrows<ConflictException> {
            authService.verify(emailAddress, verificationCode)
        }
        assert(exception.errorCode == ErrorCode.WRONG_ACCESS)
    }

    @Test
    fun `전화번호 인증시 인증요청을 하지 않고 잘몰된 접근 한 경우`() {
        val phoneNumber = TestDataFactory.createPhoneNumber()
        val verificationCode = "1234"
        whenever(phoneRepository.read(phoneNumber)).thenReturn(null)

        val exception = assertThrows<ConflictException> {
            authService.verify(phoneNumber, verificationCode)
        }
        assert(exception.errorCode == ErrorCode.WRONG_ACCESS)
    }

    @Test
    fun `로그인 정보 생성`() {
        val user = TestDataFactory.createUser()
        val result = authService.createLoginInfo(user)

        assert(result.loginType == user.status)
    }

    @Test
    fun `로그 아웃시 토큰이 삭제 되어야함 - 성공`() {
        val userId = "1234"
        val refreshToken = jwtTokenProvider.createRefreshToken(userId)

        assertDoesNotThrow {
            authService.logout(refreshToken.token)
        }
    }

    @Test
    fun `jwt 토큰 refresh에 성공해야 한다`() {
        val userId = "1234"
        val refreshToken = jwtTokenProvider.createRefreshToken(userId)
        whenever(loggedInRepository.read(refreshToken.token, userId)).thenReturn(refreshToken)

        assertDoesNotThrow {
            authService.refreshJwtToken(refreshToken.token)
        }
    }

    @Test
    fun `저장된 jwt 토큰이 없어서 에러가 발생해야 함`() {
        val userId = "1234"
        val refreshToken = jwtTokenProvider.createRefreshToken("1234")
        whenever(loggedInRepository.read(refreshToken.token, userId)).thenReturn(null)

        val exception = assertThrows<AuthorizationException> {
            authService.refreshJwtToken(refreshToken.token)
        }

        assert(exception.errorCode == ErrorCode.INVALID_TOKEN)
    }

    @Test
    fun `새로운 이메일 변경 시 다른 사람이 사용하고 있음`() {
        val userId = "1234"
        val verificationCode = "1234"
        val emailAddress = TestDataFactory.createEmailAddress()
        val email = TestDataFactory.createEmail(verificationCode)
        whenever(emailRepository.read(emailAddress)).thenReturn(email)
        whenever(userRepository.checkContactIsUsedByElse(email,userId)).thenReturn(true)


        val exception = assertThrows<ConflictException> {
            authService.createCredentialNotUsed(userId, emailAddress)
        }

        assert(exception.errorCode == ErrorCode.EMAIL_ADDRESS_IS_USED)
    }

    @Test
    fun `새로운 전화번호 변경 시 다른 사람이 사용하고 있음`() {
        val userId = "1234"
        val verificationCode = "1234"
        val phoneNumber = TestDataFactory.createPhoneNumber()
        val phone = TestDataFactory.createPhone(verificationCode)
        whenever(phoneRepository.read(phoneNumber)).thenReturn(phone)
        whenever(userRepository.checkContactIsUsedByElse(phone,userId)).thenReturn(true)

        val exception = assertThrows<ConflictException> {
            authService.createCredentialNotUsed(userId, phoneNumber)
        }

        assert(exception.errorCode == ErrorCode.PHONE_NUMBER_IS_USED)
    }

    @Test
    fun `전화번호 변경을 위한 생성시 기존에 다른사람이 사용하지 않아야 함`() {
        val userId = "1234"
        val verificationCode = "1234"
        val phoneNumber = TestDataFactory.createPhoneNumber()
        val phone = TestDataFactory.createPhone(verificationCode)
        whenever(phoneRepository.read(phoneNumber)).thenReturn(phone)
        whenever(userRepository.checkContactIsUsedByElse(phone,userId)).thenReturn(false)
        whenever(phoneRepository.appendIfNotExists(phoneNumber)).thenReturn(verificationCode)


        assertDoesNotThrow {
            authService.createCredentialNotUsed(userId, phoneNumber)
        }
    }

    @Test
    fun `이메일 변경을 위한 생성시 기존에 다른사람이 사용하지 않아야 함`() {
        val userId = "1234"
        val verificationCode = "1234"
        val emailAddress = TestDataFactory.createEmailAddress()
        val email = TestDataFactory.createEmail(verificationCode)
        whenever(emailRepository.read(emailAddress)).thenReturn(email)
        whenever(userRepository.checkContactIsUsedByElse(email,userId)).thenReturn(false)
        whenever(emailRepository.appendIfNotExists(emailAddress)).thenReturn(verificationCode)

        assertDoesNotThrow {
            authService.createCredentialNotUsed(userId, emailAddress)
        }
    }
}