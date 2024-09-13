package org.chewing.v1.implementation

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.repository.AuthRepository
import org.springframework.stereotype.Component

@Component
class AuthChecker(
    private val authRepository: AuthRepository
) {
    fun checkEmailRegistered(phoneNumber: String, email: String) {
        if (authRepository.checkEmailRegistered(phoneNumber, email)) {
            throw ConflictException(ErrorCode.EMAIL_REGISTERED)
        }
    }

    fun checkEmailVerificationCode(email: String, verificationCode: String) {
        if (!authRepository.isEmailVerificationCodeValid(email, verificationCode)) {
            throw ConflictException(ErrorCode.AUTH_1) // 인증 번호가 틀렸습니다.
        }
    }

    fun checkPhoneNumberRegistered(phoneNumber: String) {
        if (authRepository.checkPhoneNumberRegistered(phoneNumber)) {
            throw ConflictException(ErrorCode.PHONE_ALREADY_REGISTERED)
        }
    }
    fun checkPhoneVerificationCode(phoneNumber: String, verificationCode: String) {
        if (!authRepository.isPhoneVerificationCodeValid(phoneNumber, verificationCode)) {
            throw ConflictException(ErrorCode.AUTH_1) // 인증 번호가 틀렸습니다.
        }
    }



}