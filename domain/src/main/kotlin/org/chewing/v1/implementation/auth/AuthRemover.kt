package org.chewing.v1.implementation.auth

import org.chewing.v1.repository.auth.LoggedInRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AuthRemover(
    private val loggedInRepository: LoggedInRepository
) {
    @Transactional
    fun removeLoginInfo(refreshToken: String) {
        loggedInRepository.remove(refreshToken)
    }
}