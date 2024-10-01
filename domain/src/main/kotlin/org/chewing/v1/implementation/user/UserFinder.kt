package org.chewing.v1.implementation.user

import org.chewing.v1.implementation.auth.AuthReader
import org.chewing.v1.model.auth.Credential
import org.chewing.v1.model.user.User
import org.springframework.stereotype.Component

@Component
class UserFinder(
    private val userReader: UserReader,
    private val authReader: AuthReader
) {
    fun findByContact(targetContact: Credential): User {
        val contact = authReader.readContact(targetContact)
        return userReader.readByContact(contact)
    }
}