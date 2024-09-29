package org.chewing.v1.implementation.auth

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.implementation.user.UserChecker
import org.chewing.v1.model.auth.Credential
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.repository.AuthRepository
import org.springframework.stereotype.Component

@Component
class AuthValidator(
    private val authRepository: AuthRepository,
    private val userChecker: UserChecker
) {
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

    fun validateCode(contact: Contact, validateCode: String) {
        when (contact) {
            is Phone -> validatePhoneNumber(contact, validateCode)
            is Email -> validateEmail(contact, validateCode)
            else -> throw ConflictException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    fun validateIsUsed(credential: Credential, userId: String) {
        val contact = authRepository.readContact(credential)
        if(contact != null){
            userChecker.checkContactIsUsed(contact, userId)
        }
    }
}