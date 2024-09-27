package org.chewing.v1.implementation.auth

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.implementation.user.UserChecker
import org.chewing.v1.model.auth.PhoneNumber
import org.chewing.v1.repository.AuthRepository
import org.springframework.stereotype.Component

@Component
class AuthChecker(
    private val authRepository: AuthRepository,
    private val userChecker: UserChecker,
) {
    fun checkPhoneNumberIsUsed(phoneNumber: PhoneNumber, userId: String) {
        authRepository.readPhoneNumber(phoneNumber)?.let {
            if (userChecker.checkContactIsUsed(it, userId)) {
                throw ConflictException(ErrorCode.PHONE_NUMBER_IS_USED)
            }
        }
    }

    fun checkEmailIsUsed(emailAddress: String, userId: String) {
        authRepository.readEmail(emailAddress)?.let {
            if (userChecker.checkContactIsUsed(it, userId)) {
                throw ConflictException(ErrorCode.EMAIL_IS_USED)
            }
        }
    }
}