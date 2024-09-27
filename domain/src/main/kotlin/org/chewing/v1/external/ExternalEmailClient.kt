package org.chewing.v1.external

import org.chewing.v1.model.auth.EmailAddress

interface ExternalEmailClient {
    fun sendEmail(emailAddress: EmailAddress, verificationCode: String)
}