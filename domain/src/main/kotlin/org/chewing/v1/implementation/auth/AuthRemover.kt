package org.chewing.v1.implementation.auth

import org.chewing.v1.repository.AuthRepository
import org.springframework.stereotype.Component

@Component
class AuthRemover(
    private val authRepository: AuthRepository
) {
    fun removeLoginInfo(authId: String) {
        authRepository.removeLoginInfo(authId)
    }
}