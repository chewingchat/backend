package org.chewing.v1.implementation

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.repository.AuthRepository
import org.springframework.stereotype.Component
import java.util.Locale.IsoCountryCode

@Component
class AuthChecker(
    private val authRepository: AuthRepository
) {
    fun checkPhoneNumberRegistered(phoneNumber: String, countryCode: String) {
        if (authRepository.checkPhoneRegistered(phoneNumber, countryCode)) {
            throw ConflictException(ErrorCode.PHONE_ALREADY_REGISTERED)
        }
    }

    fun checkEmailRegistered(emailAddress: String) {
        if (authRepository.checkEmailRegistered(emailAddress)) {
            throw ConflictException(ErrorCode.EMAIL_REGISTERED)
        }
    }

    fun checkEmailVerificationCode(email: String, verificationCode: String) {
        if (!authRepository.isEmailVerificationCodeValid(email, verificationCode)) {
            throw ConflictException(ErrorCode.AUTH_1) // 인증 번호가 틀렸습니다.
        }
    }

    fun checkPhoneVerificationCode(phoneNumber: String, verificationCode: String) {
        if (!authRepository.isPhoneVerificationCodeValid(phoneNumber, verificationCode)) {
            throw ConflictException(ErrorCode.AUTH_1) // 인증 번호가 틀렸습니다.
        }
    }



}