package org.chewing.v1.implementation.auth

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.implementation.user.UserChecker
import org.chewing.v1.model.auth.Credential
import org.chewing.v1.model.auth.EmailAddress
import org.chewing.v1.model.auth.PhoneNumber
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.model.token.RefreshToken
import org.chewing.v1.repository.EmailRepository
import org.chewing.v1.repository.PhoneRepository
import org.springframework.stereotype.Component

@Component
class AuthValidator(
    private val userChecker: UserChecker,
    private val emailRepository: EmailRepository,
    private val phoneRepository: PhoneRepository
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
        }
    }

    fun validateIsUsed(credential: Credential, userId: String) {
        val contact = when (credential) {
            is EmailAddress -> emailRepository.read(credential)
            is PhoneNumber -> phoneRepository.read(credential)
        }
        if (contact != null) {
            userChecker.checkContactIsUsed(contact, userId)
        }
    }

    fun validateIsOwnRefreshToken(refreshToken: String, targetRefreshToken: RefreshToken){
        if(refreshToken != targetRefreshToken.token){
            throw ConflictException(ErrorCode.INVALID_TOKEN)
        }
    }
}