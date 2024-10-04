package org.chewing.v1.implementation.user

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserChecker(
    private val userRepository: UserRepository
) {
    fun checkContactIsUsed(contact: Contact, userId: String) {
        if (userRepository.checkContactIsUsedByElse(contact, userId)) {
            throw ConflictException(
                when (contact) {
                   is Email -> ErrorCode.EMAIL_IS_USED
                   is Phone -> ErrorCode.PHONE_NUMBER_IS_USED
                }
            )
        }
    }
}