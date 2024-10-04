package org.chewing.v1.implementation.auth

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.auth.Credential
import org.chewing.v1.model.auth.EmailAddress
import org.chewing.v1.model.auth.PhoneNumber
import org.chewing.v1.model.token.RefreshToken
import org.chewing.v1.repository.LoggedInRepository
import org.chewing.v1.repository.EmailRepository
import org.chewing.v1.repository.PhoneRepository
import org.springframework.stereotype.Component

@Component
class AuthAppender(
    private val loggedInRepository: LoggedInRepository,
    private val emailRepository: EmailRepository,
    private val phoneRepository: PhoneRepository
) {
    fun appendLoggedIn(newRefreshToken: RefreshToken, userId: String) {
        loggedInRepository.append(newRefreshToken, userId)
    }


    fun appendCredential(credential: Credential) {
        when(credential) {
            is EmailAddress -> emailRepository.appendIfNotExists(credential)
            is PhoneNumber -> phoneRepository.appendIfNotExists(credential)
        }
    }
    fun generateVerificationCode(credential: Credential): String {
        return when(credential) {
            is EmailAddress -> emailRepository.updateVerificationCode(credential)
            is PhoneNumber -> phoneRepository.updateVerificationCode(credential)
        }
    }
}