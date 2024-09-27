package org.chewing.v1.implementation.auth

import org.chewing.v1.model.auth.PhoneNumber
import org.chewing.v1.repository.AuthRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AuthUpdater(
    private val authRepository: AuthRepository
) {
    @Transactional
    fun updateEmailVerificationCode(email: String): String {
        // 사용자 인증 코드 업데이트
        return authRepository.updateEmailVerificationCode(email)
    }

    @Transactional
    fun updatePhoneVerificationCode(phoneNumber: PhoneNumber): String {
        // 사용자 인증 코드 업데이트
        return authRepository.updatePhoneVerificationCode(phoneNumber)
    }

    @Transactional
    fun updateEmail(email: String) {
        // 사용자 이메일 업데이트
        authRepository.updateEmail(email)
    }

    @Transactional
    fun updatePhoneNumber(phoneNumber: PhoneNumber) {
        // 사용자 휴대폰 번호 업데이트
        authRepository.updatePhoneNumber(phoneNumber)
    }
}