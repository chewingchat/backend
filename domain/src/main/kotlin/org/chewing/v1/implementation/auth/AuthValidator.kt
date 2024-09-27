package org.chewing.v1.implementation.auth

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.springframework.stereotype.Component

@Component
class AuthValidator {
    private fun validatePhoneNumber(phone: Phone, validateCode: String) {
        if (!phone.validationCode.validateCode(validateCode)) {
            throw ConflictException(ErrorCode.VALIDATE_WRONG)
        }
        if (phone.validationCode.validateExpired()) {
            throw ConflictException(ErrorCode.VALIDATE_EXPIRED)
        }
    }
    private fun validateEmail(email: Email, validateCode: String) {
        if (!email.validationCode.validateCode(validateCode)) {
            throw ConflictException(ErrorCode.VALIDATE_WRONG)
        }
        if (email.validationCode.validateExpired()) {
            throw ConflictException(ErrorCode.VALIDATE_EXPIRED)
        }
    }
    
    fun validate(contact: Contact, validateCode: String) {
        when (contact) {
            is Phone -> validatePhoneNumber(contact, validateCode)
            is Email -> validateEmail(contact, validateCode)
            else -> throw ConflictException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }
}