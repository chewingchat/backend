package org.chewing.v1.implementation.auth

import org.chewing.v1.model.user.User
import org.chewing.v1.model.contact.PhoneNumber
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

    fun appendPhone(phoneNumber: PhoneNumber) {
        return authRepository.savePhoneIfNotExists(phoneNumber)
    }

    fun appendEmail(email: String) {
        return authRepository.saveEmailIfNotExists(email)
    }
}