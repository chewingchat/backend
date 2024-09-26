package org.chewing.v1.external

import org.chewing.v1.model.contact.PhoneNumber

interface ExternalEmailClient {
    fun sendEmail(emailAddress: String, verificationCode: String)
}