package org.chewing.v1.repository

import org.chewing.v1.model.auth.EmailAddress
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.contact.Email

interface EmailRepository {
    fun saveEmailIfNotExists(emailAddress: EmailAddress)
    fun readEmail(emailAddress: EmailAddress): Contact?
    fun updateEmailVerificationCode(emailAddress: EmailAddress): String
    fun readEmailByEmailId(emailId: String): Email?
}