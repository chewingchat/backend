package org.chewing.v1.implementation.auth

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone

object AuthValidator {
    fun validatePhoneNumber(phone: Phone, validateCode: String) {
        if (!phone.validationCode.validateCode(validateCode)) {
            throw ConflictException(ErrorCode.AUTH_1)
        }
        if (phone.validationCode.validateExpired()) {
            throw ConflictException(ErrorCode.AUTH_2)
        }
    }
    fun phoneValidateIsAuthorizedFirst(phone: Phone) {
        if (phone.isAuthorizedFirst) {
            throw ConflictException(ErrorCode.AUTH_3)
        }
    }
    fun emailValidateIsAuthorizedFirst(email: Email) {
        if (email.isAuthorizedFirst) {
            throw ConflictException(ErrorCode.AUTH_3)
        }
    }

    fun validateEmail(email: Email, validateCode: String) {
        if (!email.validationCode.validateCode(validateCode)) {
            throw ConflictException(ErrorCode.AUTH_1)
        }
        if (email.validationCode.validateExpired()) {
            throw ConflictException(ErrorCode.AUTH_2)
        }
    }
}