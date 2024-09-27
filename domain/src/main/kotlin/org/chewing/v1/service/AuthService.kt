package org.chewing.v1.service

import org.chewing.v1.implementation.auth.*
import org.chewing.v1.implementation.user.UserProcessor
import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.model.auth.JwtToken
import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.model.auth.PhoneNumber
import org.chewing.v1.model.user.User

import org.springframework.stereotype.Service


@Service
class AuthService(
    private val authChecker: AuthChecker,
    private val userProcessor: UserProcessor,
    private val jwtTokenProvider: JwtTokenProvider,
    private val authReader: AuthReader,
    private val authAppender: AuthAppender,
    private val authRemover: AuthRemover,
    private val userReader: UserReader,
    private val authUpdater: AuthUpdater,
    private val authSender: AuthSender,
    private val authValidator: AuthValidator,
    private val authProcessor: AuthProcessor
) {
    fun sendPhoneVerification(phoneNumber: PhoneNumber) {
        authAppender.appendPhone(phoneNumber)
        val verificationCode = authUpdater.updatePhoneVerificationCode(phoneNumber)
        authSender.sendPhoneVerificationCode(phoneNumber, verificationCode)
    }

    fun sendEmailVerification(emailAddress: String) {
        authAppender.appendEmail(emailAddress)
        val verificationCode = authUpdater.updateEmailVerificationCode(emailAddress)
        authSender.sendEmailVerificationCode(emailAddress, verificationCode)
    }

    fun verifyPhone(
        phoneNumber: PhoneNumber,
        verificationCode: String,
        appToken: String,
        device: PushToken.Device
    ): Pair<JwtToken, User> {
        val savedPhone = authReader.readCredential(phoneNumber)
        // 받은 휴대폰 인증 번호 비교하여 검증 -> 틀리면 예외 발생, 유효시간 초과시 예외 발생
        authValidator.validate(savedPhone, verificationCode)
        // 로그인 처리
        val (token,user) = authProcessor.processLogin(savedPhone)
        //푸시 토큰 처리
        userProcessor.processPushToken(user, appToken, device)
        return Pair(token, user)
    }

    //밑에 verifyEmailAndLogin, verifyEmailAndSignup, verifyPhoneAndSignup, 수정
    fun verifyEmail(
        emailAddress: String,
        verificationCode: String,
        appToken: String,
        device: PushToken.Device
    ): Pair<JwtToken, User> {

        // 사용자가 인증코드 입력 후 이메일 인증 번호 확인용 읽기(인증번호 받음)
        val savedEmail = authReader.readEmail(emailAddress)
        // 받은 이메일 인증 번호 비교하여 검증 -> 틀리면 예외 발생, 유효시간 초과시 예외 발생
        authValidator.validate(savedEmail, verificationCode)
        // 로그인 처리
        val (token,user) = authProcessor.processLogin(savedEmail)
        //푸시 토큰 처리
        userProcessor.processPushToken(user, appToken, device)
        return Pair(token, user)
    }

    fun logout(accessToken: String) {
        val userId = jwtTokenProvider.getUserIdFromToken(accessToken)
        // 사용자 로그인 정보 삭제 (리프레시 토큰 포함)
        authRemover.removeLoginInfo(userId)
    }

    fun refreshAccessToken(refreshToken: String): JwtToken {
        val token = jwtTokenProvider.cleanedToken(refreshToken)
        // 리프레시 토큰 유효성 검사(수정)
        jwtTokenProvider.validateRefreshToken(token)
        // 리프레시 토큰에서 사용자 ID 추출
        val userId = jwtTokenProvider.getUserIdFromToken(token)

        val user = userReader.read(userId)

        val newToken = jwtTokenProvider.createJwtToken(user.userId)

        authAppender.appendLoggedIn(newToken.refreshToken, user)

        return newToken
    }

    fun sendPhoneVerificationForUpdate(userId: String, phoneNumber: PhoneNumber) {
        // 번호 중복 체크 로직 (이미 사용 중인 번호인지 확인)
        authChecker.checkPhoneNumberIsUsed(phoneNumber, userId)

        authAppender.appendPhone(phoneNumber)
        val verificationCode = authUpdater.updatePhoneVerificationCode(phoneNumber)
        authSender.sendPhoneVerificationCode(phoneNumber, verificationCode)
    }

    fun verifyPhoneForUpdate(userId: String, phoneNumber: PhoneNumber, verificationCode: String) {
        // 인증 정보 읽기
        val savedPhone = authReader.readPhoneNumber(phoneNumber)
        // 인증번호 검증
        authValidator.validate(savedPhone, verificationCode)
        // 업데이트 로직 실행
        authUpdater.updatePhoneNumber(phoneNumber)
    }

    // 이메일 수정 전 인증 요청 로직
    fun sendEmailVerificationForUpdate(userId: String, emailAddress: String) {
        // 이메일 중복 체크 로직 (이미 사용 중인 이메일인지 확인)
        authChecker.checkEmailIsUsed(emailAddress, userId)
        authAppender.appendEmail(emailAddress)
        val verificationCode = authUpdater.updateEmailVerificationCode(emailAddress)
        authSender.sendEmailVerificationCode(emailAddress, verificationCode)
    }

    // 이메일 인증 및 수정 로직
    fun verifyEmailForUpdate(userId: String, email: String, verificationCode: String) {
        // 인증 정보 읽기
        val savedEmail = authReader.readEmail(email)
        // 인증번호 검증
        authValidator.validate(savedEmail, verificationCode)
        // 업데이트 로직 실행
        authUpdater.updateEmail(email)
    }
}