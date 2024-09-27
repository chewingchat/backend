package org.chewing.v1.implementation.auth

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.auth.Credential
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.repository.AuthRepository
import org.springframework.stereotype.Component


@Component
class AuthReader(
    private val authRepository: AuthRepository,
) {
    fun readContact(credential: Credential): Contact {
        return authRepository.readContact(credential) ?: throw ConflictException(ErrorCode.INTERNAL_SERVER_ERROR)
    }
}