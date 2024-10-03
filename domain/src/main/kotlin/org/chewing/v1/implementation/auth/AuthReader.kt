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
import org.chewing.v1.model.token.RefreshToken
import org.chewing.v1.repository.EmailRepository
import org.chewing.v1.repository.LoggedInRepository
import org.chewing.v1.repository.PhoneRepository
import org.springframework.stereotype.Component


@Component
class AuthReader(
    private val phoneRepository: PhoneRepository,
    private val emailRepository: EmailRepository,
    private val loggedInRepository: LoggedInRepository
) {
    fun readContact(targetContact: Credential): Contact {
        return when (targetContact) {
            is EmailAddress -> emailRepository.read(targetContact)
                ?: throw NotFoundException(ErrorCode.USER_NOT_FOUND)

            is PhoneNumber -> phoneRepository.read(targetContact)
                ?: throw NotFoundException(ErrorCode.USER_NOT_FOUND)

            else -> throw ConflictException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    fun readRefreshToken(refreshToken: String): RefreshToken {
        return loggedInRepository.read(refreshToken) ?: throw ConflictException(ErrorCode.INVALID_TOKEN)
    }

    fun readEmailByEmailId(emailId: String): Email? {
        return emailRepository.readById(emailId)
    }

    fun readPhoneByPhoneNumberId(phoneNumberId: String): Phone? {
        return phoneRepository.readById(phoneNumberId)
    }
}