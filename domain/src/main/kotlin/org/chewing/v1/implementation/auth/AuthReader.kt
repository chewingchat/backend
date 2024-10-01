package org.chewing.v1.implementation.auth

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.auth.Credential
import org.chewing.v1.model.auth.EmailAddress
import org.chewing.v1.model.auth.PhoneNumber
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.repository.AuthRepository
import org.springframework.stereotype.Component


@Component
class AuthReader(
    private val authRepository: AuthRepository,
) {
    fun readContact(targetContact: Credential): Contact {
        return authRepository.readContact(targetContact) ?: throw NotFoundException(ErrorCode.USER_NOT_FOUND)
    }

    fun readLoggedInId(refreshToken: String): String {
        return authRepository.readLoggedId(refreshToken) ?: throw ConflictException(ErrorCode.INVALID_TOKEN)
    }

    fun readEmailByEmailId(emailId: String): Email? {
        return authRepository.readContactByEmailId(emailId)
    }

    fun readPhoneByPhoneNumberId(phoneNumberId: String): Phone? {
        return authRepository.readContactByPhoneNumberId(phoneNumberId)
    }
}