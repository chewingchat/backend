package org.chewing.v1.implementation.auth

import org.chewing.v1.model.auth.Credential
import org.chewing.v1.model.user.User
import org.chewing.v1.model.auth.PhoneNumber
import org.chewing.v1.model.token.RefreshToken
import org.chewing.v1.repository.AuthRepository
import org.springframework.stereotype.Component

@Component
class AuthAppender(
    private val authRepository: AuthRepository
) {
    fun appendLoggedIn(refreshToken: RefreshToken, user: User) {
        authRepository.appendLoggedIn(refreshToken, user)
    }

    fun appendCredential(credential: Credential) {
        authRepository.saveCredentialIfNotExists(credential)
    }
}