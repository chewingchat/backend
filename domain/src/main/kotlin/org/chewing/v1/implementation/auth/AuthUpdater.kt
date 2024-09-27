package org.chewing.v1.implementation.auth

import org.chewing.v1.model.auth.Credential
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.repository.AuthRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AuthUpdater(
    private val authRepository: AuthRepository
) {
    @Transactional
    fun updateVerificationCode(credential: Credential): String {
        return authRepository.updateVerificationCode(credential)
    }
}