package org.chewing.v1.implementation.user

import org.chewing.v1.implementation.auth.AuthReader
import org.chewing.v1.model.auth.EmailAddress
import org.chewing.v1.model.user.User
import org.chewing.v1.model.auth.PhoneNumber
import org.springframework.stereotype.Component

@Component
class UserFinder(
    private val userReader: UserReader,
    private val authReader: AuthReader
) {
    fun findUserByEmail(emailAddress: EmailAddress): User {
        val email = authReader.readContact(emailAddress)
        return userReader.readByContact(email)
    }

    fun findUserByPhoneNumber(phoneNumber: PhoneNumber): User {
        val phone = authReader.readContact(phoneNumber)
        return userReader.readByContact(phone)
    }
}