package org.chewing.v1.implementation.user

import org.chewing.v1.model.contact.Contact
import org.chewing.v1.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserChecker(
    private val userRepository: UserRepository
) {
    fun checkContactIsUsed(contact: Contact, userId: String): Boolean {
        return userRepository.checkContactIsUsedByElse(contact, userId)
    }
}