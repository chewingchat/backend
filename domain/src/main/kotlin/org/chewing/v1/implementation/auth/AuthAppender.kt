package org.chewing.v1.implementation.auth

import org.chewing.v1.model.auth.AuthInfo
import org.chewing.v1.model.contact.PhoneNumber
import org.chewing.v1.model.token.RefreshToken
import org.chewing.v1.repository.AuthRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthAppender(
    private val authRepository: AuthRepository
) {
    fun appendLoggedInInfo(authInfo: AuthInfo, refreshToken: RefreshToken) {
        authRepository.appendLoggedInInfo(authInfo, refreshToken)
    }

    // 새로운 함수: 휴대폰 인증 정보를 저장하는 함수
    fun appendPhoneVerification(phoneNumber: PhoneNumber): String {
        return authRepository.savePhoneVerification(phoneNumber) // AuthInfo 엔티티를 저장하는 로직 (아래 저장 로직도 구현)
    }

    fun appendEmailVerification(email: String): String {
        return authRepository.saveEmailVerification(email) // AuthInfo 엔티티를 저장하는 로직 (아래 저장 로직도 구현)
    }

    fun appendAuthInfoByEmailId(emailId: String, userId: String): AuthInfo {
        return authRepository.saveAuthInfoByEmailId(emailId, userId)
    }

    fun appendAuthInfoByPhoneId(phoneNumberId: String, userId: String): AuthInfo {
        return authRepository.saveAuthInfoByPhoneNumberId(phoneNumberId, userId)
    }
}