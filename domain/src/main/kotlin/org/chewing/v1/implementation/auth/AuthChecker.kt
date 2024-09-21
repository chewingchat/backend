package org.chewing.v1.implementation.auth

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.contact.PhoneNumber
import org.chewing.v1.repository.AuthRepository
import org.springframework.stereotype.Component
import java.util.Locale.IsoCountryCode

@Component
class AuthChecker(
    private val authRepository: AuthRepository
) {
    fun checkPhoneNumberRegistered(phoneNumber: PhoneNumber) {
        if (authRepository.checkPhoneRegistered(phoneNumber)) {
            throw ConflictException(ErrorCode.PHONE_ALREADY_REGISTERED)
        }
    }

    fun checkEmailRegistered(emailAddress: String) {
        if (authRepository.checkEmailRegistered(emailAddress)) {
            throw ConflictException(ErrorCode.EMAIL_REGISTERED)
        }
    }
}