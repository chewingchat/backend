package org.chewing.v1.implementation.auth

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.implementation.user.UserChecker
import org.chewing.v1.model.auth.Credential
import org.chewing.v1.model.contact.ContactType
import org.chewing.v1.repository.AuthRepository
import org.springframework.stereotype.Component

@Component
class AuthChecker(
    private val authRepository: AuthRepository,
    private val userChecker: UserChecker,
) {
    fun checkCredentialIsUsed(credential: Credential, userId: String) {
        val contact = authRepository.readContact(credential)
        if (contact != null && userChecker.checkContactIsUsed(contact, userId)) {
            throw ConflictException(
                when (contact.type) {
                    ContactType.EMAIL -> ErrorCode.EMAIL_IS_USED
                    ContactType.PHONE -> ErrorCode.PHONE_NUMBER_IS_USED
                }
            )
        }
    }
}