package org.chewing.v1.implementation.auth

import org.chewing.v1.model.contact.PhoneNumber
import org.chewing.v1.repository.AuthRepository
import org.springframework.stereotype.Component

@Component
class AuthUpdater(
    private val authRepository: AuthRepository
) {

    fun updatePhoneAuthorized(phoneId: String) {
        // 사용자 최초 인증 승인
        authRepository.updatePhoneAuthorized(phoneId)
    }

    fun updateEmailAuthorized(emailId: String) {
        // 사용자 최초 인증 승인
        authRepository.updateEmailAuthorized(emailId)
    }

    fun updateEmailVerificationCode(email: String) {
        // 사용자 인증 코드 업데이트
        authRepository.updateEmailVerificationCode(email)
    }
    fun updatePhoneVerificationCode(phoneNumber: PhoneNumber) {
        // 사용자 인증 코드 업데이트
        authRepository.updatePhoneVerificationCode(phoneNumber)
    }
    fun updateEmail(email: String) {
        // 사용자 이메일 업데이트
        authRepository.updateEmail(email)
    }
    fun updatePhoneNumber(phoneNumber: PhoneNumber) {
        // 사용자 휴대폰 번호 업데이트
        authRepository.updatePhoneNumber(phoneNumber)
    }
}