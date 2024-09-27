package org.chewing.v1.service

import org.chewing.v1.implementation.auth.*
import org.chewing.v1.implementation.user.UserProcessor
import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.implementation.user.UserUpdater
import org.chewing.v1.model.auth.*
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
    private val authProcessor: AuthProcessor,
    private val userUpdater: UserUpdater
) {
    fun sendVerification(credential: Credential) {
        authAppender.appendCredential(credential)
        val verificationCode = authUpdater.updateVerificationCode(credential)
        authSender.sendVerificationCode(credential, verificationCode)
    }

    fun verifyLogin(
        credential: Credential, verificationCode: String,
        appToken: String,
        device: PushToken.Device
    ): Pair<JwtToken, User> {
        val savedCredential = authReader.readContact(credential)
        authValidator.validate(savedCredential, verificationCode)
        val (token, user) = authProcessor.processLogin(savedCredential)
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

    fun sendVerificationForUpdate(userId: String, credential: Credential) {
        authChecker.checkCredentialIsUsed(credential, userId)
        authAppender.appendCredential(credential)
        val verificationCode = authUpdater.updateVerificationCode(credential)
        authSender.sendVerificationCode(credential, verificationCode)
    }

    fun verifyCredentialForUpdate(userId: String, credential: Credential, verificationCode: String) {
        // 인증 정보 읽기
        val savedCredential = authReader.readContact(credential)
        // 인증번호 검증
        authValidator.validate(savedCredential, verificationCode)
        // 업데이트 로직 실행
        userUpdater.updateContact(userId, savedCredential)
    }
}