package org.chewing.v1.implementation

import org.chewing.v1.model.AuthInfo
import org.chewing.v1.model.Email
import org.chewing.v1.model.Phone
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
    fun appendPhoneVerificationInfo(phone: Phone) {
        val authInfo = AuthInfo.of(UUID.randomUUID().toString(), phone, Email.empty()) // 인증 정보를 생성 (이메일은 빈 값)
        authRepository.savePhoneVerificationInfo(authInfo) // AuthInfo 엔티티를 저장하는 로직 (아래 저장 로직도 구현)
    }
    fun appendEmailVerificationInfo(email: Email) {
        val authInfo = AuthInfo.of(UUID.randomUUID().toString(), Phone.empty(), email) // 인증 정보를 생성 (이메일은 빈 값)
        authRepository.saveEmailVerificationInfo(authInfo) // AuthInfo 엔티티를 저장하는 로직 (아래 저장 로직도 구현)
    }


}