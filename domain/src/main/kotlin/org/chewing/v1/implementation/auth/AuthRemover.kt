package org.chewing.v1.implementation.auth

import org.chewing.v1.repository.AuthRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AuthRemover(
    private val authRepository: AuthRepository
) {
    @Transactional
    fun removeLoginInfo(authId: String) {
        authRepository.removeLoginInfo(authId)
    }
    @Transactional
    fun removeAll(authId: String) {
        authRepository.removeAuthInfo(authId)
    }
}