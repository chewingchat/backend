package org.chewing.v1.service

import org.chewing.v1.implementation.auth.*
import org.chewing.v1.implementation.media.FileGenerator
import org.chewing.v1.implementation.media.FileProcessor
import org.chewing.v1.implementation.user.UserProcessor
import org.chewing.v1.implementation.user.UserUpdater
import org.chewing.v1.model.auth.*
import org.chewing.v1.model.media.FileData

import org.springframework.stereotype.Service
import java.io.File


@Service
class AuthService(
    private val userProcessor: UserProcessor,
    private val authReader: AuthReader,
    private val authAppender: AuthAppender,
    private val authUpdater: AuthUpdater,
    private val authSender: AuthSender,
    private val authValidator: AuthValidator,
    private val authProcessor: AuthProcessor,
    private val userUpdater: UserUpdater,
    private val fileProcessor: FileProcessor
) {
    fun makeCredential(credential: Credential) {
        authAppender.appendCredential(credential)
        val verificationCode = authUpdater.updateVerificationCode(credential)
        authSender.sendVerificationCode(credential, verificationCode)
    }

    fun login(
        credential: Credential, verificationCode: String,
        appToken: String,
        device: PushToken.Device,
    ): LoginInfo {
        val existingCredential = authReader.readContact(credential)
        authValidator.validate(existingCredential, verificationCode)
        val (token, user) = authProcessor.processLogin(existingCredential)
        userProcessor.processPushToken(user, appToken, device)
        return LoginInfo.of(token, user)
    }

    fun logout(accessToken: String) {
        authProcessor.processLogout(accessToken)
    }

    fun refreshJwtToken(refreshToken: String): JwtToken {
        val (newToken, userId) = authProcessor.processRefreshToken(refreshToken)
        authAppender.appendLoggedIn(newToken.refreshToken, userId)
        return newToken
    }

    fun makeUnusedCredential(userId: String, credential: Credential) {
        authValidator.validateIsUsed(credential, userId)
        authAppender.appendCredential(credential)
        val verificationCode = authUpdater.updateVerificationCode(credential)
        authSender.sendVerificationCode(credential, verificationCode)
    }

    fun changeCredential(userId: String, credential: Credential, verificationCode: String) {
        // 인증 정보 읽기
        val existingCredential = authReader.readContact(credential)
        // 인증번호 검증
        authValidator.validate(existingCredential, verificationCode)
        // 업데이트 로직 실행
        userUpdater.updateContact(userId, existingCredential)
    }
}