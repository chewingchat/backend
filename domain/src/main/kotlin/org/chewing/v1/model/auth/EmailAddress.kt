package org.chewing.v1.model.auth

import org.chewing.v1.model.contact.ContactType

class EmailAddress private constructor(
    val email: String
): Credential {
    companion object {
        fun of(
            email: String
        ): EmailAddress {
            return EmailAddress(
                email = email
            )
        }
    }

    override val type: ContactType
        get() = ContactType.EMAIL
}