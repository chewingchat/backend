package org.chewing.v1.model.user

import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone

class UserProfile private constructor(
    val user: User,
    val emailAddress: String,
    val number: String,
    val countryCode: String,
) {
    companion object {
        fun of(
            user: User,
            email: Email?,
            phone: Phone?,
        ): UserProfile {
            return UserProfile(
                user = user,
                emailAddress = email?.emailAddress ?: "none",
                number = phone?.number ?: "none",
                countryCode = phone?.countryCode ?: "none"
            )
        }
    }
}