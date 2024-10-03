package org.chewing.v1.repository

import org.chewing.v1.model.auth.EmailAddress
import org.chewing.v1.model.contact.Email

interface EmailRepository {
    fun appendIfNotExists(emailAddress: EmailAddress)
    fun read(emailAddress: EmailAddress): Email?
    fun updateVerificationCode(emailAddress: EmailAddress): String
    fun readById(emailId: String): Email?
}