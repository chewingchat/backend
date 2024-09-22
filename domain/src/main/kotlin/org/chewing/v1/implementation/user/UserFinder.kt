package org.chewing.v1.implementation.user

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.implementation.auth.AuthReader
import org.chewing.v1.model.User
import org.chewing.v1.model.contact.Contact
import org.springframework.stereotype.Component

@Component
class UserFinder(
    private val userReader: UserReader,
    private val authReader: AuthReader
) {
    fun findUserByContact(contact: Contact): User {
        val authInfo = authReader.readByContact(contact)?:throw NotFoundException(ErrorCode.USER_NOT_FOUND)
        return userReader.read(authInfo.userId)
    }
}