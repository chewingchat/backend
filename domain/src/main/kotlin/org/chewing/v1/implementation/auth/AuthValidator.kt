package org.chewing.v1.implementation.auth

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone

object AuthValidator {
    fun validatePhoneNumber(phone: Phone, validateCode: String) {
        if (!phone.validationCode.validateCode(validateCode)) {
            throw ConflictException(ErrorCode.VALIDATE_WRONG)
        }
        if (phone.validationCode.validateExpired()) {
            throw ConflictException(ErrorCode.VALIDATE_EXPIRED)
        }
    }
    fun validateEmail(email: Email, validateCode: String) {
        if (!email.validationCode.validateCode(validateCode)) {
            throw ConflictException(ErrorCode.VALIDATE_WRONG)
        }
        if (email.validationCode.validateExpired()) {
            throw ConflictException(ErrorCode.VALIDATE_EXPIRED)
        }
    }
}